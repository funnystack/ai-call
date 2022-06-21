package com.funny.combo.ai.call.service.asr;

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.NlsClient;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriber;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberListener;
import com.alibaba.nls.client.protocol.asr.SpeechTranscriberResponse;
import com.funny.combo.ai.call.config.ai.NlsClientConfig;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import static com.funny.combo.ai.call.config.ai.NlsClientConfig.accessKeyId;
import static com.funny.combo.ai.call.config.ai.NlsClientConfig.accessKeySecret;

/**
 * 此示例演示了：
 * ASR实时识别API调用。
 * 动态获取token。
 * 通过本地模拟实时流发送。
 * 识别耗时计算。
 */
public class SpeechTranscriberDemo {
    private NlsClient client;
    private static final Logger logger = LoggerFactory.getLogger(SpeechTranscriberDemo.class);
    public static final List<Long> timesList = Lists.newArrayList();

    public SpeechTranscriberDemo(String id, String secret) {
        //应用全局创建一个NlsClient实例，默认服务地址为阿里云线上服务地址。
        //获取token，实际使用时注意在accessToken.getExpireTime()过期前再次获取。
        AccessToken accessToken = new AccessToken(id, secret);
        try {
            accessToken.apply();
            System.out.println("get token: " + ", expire time: " + accessToken.getExpireTime());
            client = new NlsClient(accessToken.getToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static SpeechTranscriberListener getTranscriberListener() {
        SpeechTranscriberListener listener = new SpeechTranscriberListener() {
            //识别出中间结果。仅当setEnableIntermediateResult为true时，才会返回该消息。
            @Override
            public void onTranscriptionResultChange(SpeechTranscriberResponse response) {
//                System.out.println("task_id: " + response.getTaskId() +
//                    ", name: " + response.getName() +
//                    //状态码“20000000”表示正常识别。
//                    ", status: " + response.getStatus() +
//                    //句子编号，从1开始递增。
//                    ", index: " + response.getTransSentenceIndex() +
//                    //当前的识别结果。
//                    ", result: " + response.getTransSentenceText() +
//                    //当前已处理的音频时长，单位为毫秒。
//                    ", time: " + response.getTransSentenceTime());
            }

            @Override
            public void onTranscriberStart(SpeechTranscriberResponse response) {
                //task_id是调用方和服务端通信的唯一标识，遇到问题时，需要提供此task_id。
//                System.out.println("task_id: " + response.getTaskId() + ", name: " + response.getName() + ", status: " + response.getStatus());
            }

            @Override
            public void onSentenceBegin(SpeechTranscriberResponse response) {
//                System.out.println("task_id: " + response.getTaskId() + ", name: " + response.getName() + ", status: " + response.getStatus());

            }

            //识别出一句话。服务端会智能断句，当识别到一句话结束时会返回此消息。
            @Override
            public void onSentenceEnd(SpeechTranscriberResponse response) {
                System.out.println("task_id: " + response.getTaskId() +
                        ", name: " + response.getName() +
                        //状态码“20000000”表示正常识别。
                        ", status: " + response.getStatus() +
                        //句子编号，从1开始递增。
                        ", index: " + response.getTransSentenceIndex() +
                        //当前的识别结果。
                        ", result: " + response.getTransSentenceText() +
                        //置信度
                        ", confidence: " + response.getConfidence() +
                        //开始时间
                        ", begin_time: " + response.getSentenceBeginTime() +
                        //当前已处理的音频时长，单位为毫秒。
                        ", time: " + response.getTransSentenceTime());
            }

            //识别完毕
            @Override
            public void onTranscriptionComplete(SpeechTranscriberResponse response) {
//                System.out.println("task_id: " + response.getTaskId() + ", name: " + response.getName() + ", status: " + response.getStatus());
            }

            @Override
            public void onFail(SpeechTranscriberResponse response) {
                //task_id是调用方和服务端通信的唯一标识，遇到问题时，需要提供此task_id。
//                System.out.println("task_id: " + response.getTaskId() +  ", status: " + response.getStatus() + ", status_text: " + response.getStatusText());
            }
        };

        return listener;
    }

    //根据二进制数据大小计算对应的同等语音长度。
    //sampleRate：支持8000或16000。
    public static int getSleepDelta(int dataSize, int sampleRate) {
        // 仅支持16位采样。
        int sampleBytes = 16;
        // 仅支持单通道。
        int soundChannel = 1;
        return (dataSize * 10 * 8000) / (160 * sampleRate);
    }

    public void process(String filepath) {
        SpeechTranscriber transcriber = null;
        try {
            //创建实例、建立连接。
            transcriber = new SpeechTranscriber(client, getTranscriberListener());
            transcriber.setAppKey(NlsClientConfig.appKey);
            //输入音频编码方式
            transcriber.setFormat(InputFormatEnum.PCM);
            //输入音频采样率
            transcriber.setSampleRate(SampleRateEnum.SAMPLE_RATE_8K);
            //是否返回中间识别结果
            transcriber.setEnableIntermediateResult(true);
            //是否生成并返回标点符号
            transcriber.setEnablePunctuation(true);
            //是否将返回结果规整化,比如将一百返回为100
            transcriber.setEnableITN(true);
//            //设置vad断句参数。默认值：800ms，有效值：200ms～2000ms。
//            transcriber.addCustomedParam("max_sentence_silence", 600);
//            //设置是否语义断句。
//            transcriber.addCustomedParam("enable_semantic_sentence_detection",false);
//            //设置是否开启顺滑。
//            transcriber.addCustomedParam("disfluency",true);
            //设置训练后的定制热词id。
            transcriber.addCustomedParam("vocabulary_id", "90ce5b718f4f4e77817e4c4960695968");
//            //设置vad噪音阈值参数，参数取值为-1～+1，如-0.9、-0.8、0.2、0.9。
//            //取值越趋于-1，判定为语音的概率越大，亦即有可能更多噪声被当成语音被误识别。
//            //取值越趋于+1，判定为噪音的越多，亦即有可能更多语音段被当成噪音被拒绝识别。
//            //该参数属高级参数，调整需慎重和重点测试。
//            transcriber.addCustomedParam("speech_noise_threshold",0.3);
//            //设置训练后的定制语言模型id。
//            transcriber.addCustomedParam("customization_id","ce4745d479814711ae33686168010690");
            //此方法将以上参数设置序列化为JSON发送给服务端，并等待服务端确认。
            transcriber.start();

            File file = new File(filepath);
            FileInputStream fis = new FileInputStream(file);
            byte[] b = new byte[3200];
            int len;
            while ((len = fis.read(b)) > 0) {
//                logger.info("send data pack length: " + len);
                transcriber.send(b, len);
                //本案例用读取本地文件的形式模拟实时获取语音流并发送的，因为读取速度较快，这里需要设置sleep。
                //如果实时获取语音则无需设置sleep, 如果是8k采样率语音第二个参数设置为8000。
                int deltaSleep = getSleepDelta(len, 8000);
                Thread.sleep(deltaSleep);
            }

            //通知服务端语音数据发送完毕，等待服务端处理完成。
            long now = System.currentTimeMillis();
//            logger.info("ASR wait for complete");
            transcriber.stop();
            Long times = (System.currentTimeMillis() - now);
            timesList.add(times);
            logger.info("ASR latency : " + times + " ms");
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            if (null != transcriber) {
                transcriber.close();
            }
        }
    }

    public void shutdown() {
        client.shutdown();
    }

    public static void main(String[] args) throws Exception {
        String id = accessKeyId;
        String secret = accessKeySecret;
        //本案例使用本地文件模拟发送实时流数据。您在实际使用时，可以实时采集或接收语音流并发送到ASR服务端。
        String filepath = "/Users/fangli/Documents/10.pcm";
        for (int i = 0; i < 1; i++) {
            SpeechTranscriberDemo demo = new SpeechTranscriberDemo(id, secret);
            demo.process(filepath);
            demo.shutdown();
        }
    }
}
