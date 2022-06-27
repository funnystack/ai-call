package com.funny.combo.ai.call.service.asr;

import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizer;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizerListener;
import com.alibaba.nls.client.protocol.asr.SpeechRecognizerResponse;
import com.funny.combo.ai.call.common.BaseResult;
import com.funny.combo.ai.call.config.ai.AliClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.funny.combo.ai.call.config.ai.NlsClientConfig.appKey;

@Service
public class AliAsrService implements AsrService {
    private static final Logger logger = LoggerFactory.getLogger(AliAsrService.class);

    @Resource
    private AliClientFactory aliClientFactory;

    @Override
    public BaseResult<AsrResult> asr(AsrRequest asrRequest) {
        File file = new File(asrRequest.getFilePath());
        if (!file.exists()) {
            return BaseResult.error(asrRequest.getFilePath() + "语音文件不存在");
        }
        AsrPromise asrPromise = new AsrPromise(asrRequest);
        SpeechRecognizer recognizer = null;
        Long start = System.currentTimeMillis();
        try {
            SpeechRecognizerListener listener = getRecognizerListener(asrPromise);
            recognizer = new SpeechRecognizer(aliClientFactory.getNlsClient(), listener);
            recognizer.setAppKey(appKey);
            //设置音频编码格式 如果是opus文件，请设置为 InputFormatEnum.OPUS
            recognizer.setFormat(InputFormatEnum.PCM);
            //设置音频采样率
            if (asrRequest.getSampleRate() == 16000) {
                recognizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            } else if (asrRequest.getSampleRate() == 8000) {
                recognizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_8K);
            }
            //设置是否返回中间识别结果
            recognizer.setEnableIntermediateResult(true);
            //此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            long now = System.currentTimeMillis();
            recognizer.start();
            long startLatency = (System.currentTimeMillis() - now);
            logger.info("ali asr start latency : " + startLatency + " ms");
            // 读取文件 发送数据
            byte[] bytes = Files.readAllBytes(Paths.get(asrRequest.getFilePath()));
            recognizer.send(bytes);
            //通知服务端语音数据发送完毕,等待服务端处理完成
            now = System.currentTimeMillis();
            // 计算实际延迟: stop返回之后一般即是识别结果返回时间
            logger.info("ali asr wait for complete");
            recognizer.stop();
            long stopLatency = (System.currentTimeMillis() - now);
            logger.info("ali asr stop latency : " + stopLatency + " ms");
            synchronized (asrPromise) {
                asrPromise.wait(1000);
            }
            AsrResult asrResult = asrPromise.getAsrResult();
            if (asrResult == null) {
                asrResult = new AsrResult();
            }
            asrResult.setStartLatency(startLatency);
            asrResult.setStopLatency(stopLatency);
            asrResult.setUsed(System.currentTimeMillis() - start);
            asrPromise.setAsrResult(asrResult);
            return BaseResult.OK(asrPromise.getAsrResult());
        } catch (Exception e) {
            e.printStackTrace();
            return BaseResult.error("ali asr error:" + e.getMessage());
        } finally {
            //关闭连接
            if (null != recognizer) {
                recognizer.close();
            }
        }

    }

    // 传入自定义参数
    private static SpeechRecognizerListener getRecognizerListener(AsrPromise asrPromise) {
        AsrResult asrResult = new AsrResult();
        SpeechRecognizerListener listener = new SpeechRecognizerListener() {
            //识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
            @Override
            public void onRecognitionResultChanged(SpeechRecognizerResponse response) {
                //事件名称 RecognitionResultChanged、 状态码(20000000 表示识别成功)、语音识别文本
                System.out.println("name: " + response.getName() + ", status: " + response.getStatus() + ", result: " + response.getRecognizedText());
            }

            //识别完毕
            @Override
            public void onRecognitionCompleted(SpeechRecognizerResponse response) {
                //事件名称 RecognitionCompleted, 状态码 20000000 表示识别成功, getRecognizedText是识别结果文本
                System.out.println("name: " + response.getName() + ", status: " + response.getStatus() + ", result: " + response.getRecognizedText());
                asrResult.setCompleteTime(new Date());
                asrResult.setStatus(response.getStatus());
                asrResult.setStatusText(response.getStatusText());
                asrResult.setText(response.getRecognizedText());
                asrPromise.setAsrResult(asrResult);
                synchronized (asrPromise) {
                    asrPromise.notify();
                }
            }

            @Override
            public void onStarted(SpeechRecognizerResponse response) {
                asrResult.setStartTime(new Date());
                asrResult.setTask_id(response.getTaskId());
            }

            @Override
            public void onFail(SpeechRecognizerResponse response) {
                asrResult.setCompleteTime(new Date());
                asrResult.setStatus(response.getStatus());
                asrResult.setStatusText(response.getStatusText());
                asrResult.setTask_id(response.getTaskId());
                asrPromise.setAsrResult(asrResult);
                synchronized (asrPromise) {
                    asrPromise.notify();
                }
            }
        };
        return listener;
    }

    /// 根据二进制数据大小计算对应的同等语音长度
    /// sampleRate 仅支持8000或16000
    public static int getSleepDelta(int dataSize, int sampleRate) {
        // 仅支持16位采样
        int sampleBytes = 16;
        // 仅支持单通道
        int soundChannel = 1;
        return (dataSize * 10 * 8000) / (160 * sampleRate);
    }

}
