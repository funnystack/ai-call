package com.funny.combo.ai.call.service.asr;

import com.alibaba.nls.client.protocol.OutputFormatEnum;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizer;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerListener;
import com.alibaba.nls.client.protocol.tts.SpeechSynthesizerResponse;
import com.funny.combo.ai.call.config.ai.AliClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.funny.combo.ai.call.config.ai.NlsClientConfig.appKey;

@Component
public class AliTTSService {
    private static final Logger logger = LoggerFactory.getLogger(AliTTSService.class);

    @Resource
    private AliClientFactory aliClientFactory;

    private static long startTime;

    public void ttsProcess(String text) {
        SpeechSynthesizer synthesizer = null;
        try {
            //创建实例,建立连接
            synthesizer = new SpeechSynthesizer(aliClientFactory.getNlsClient(), getSynthesizerListener());
            synthesizer.setAppKey(appKey);
            //设置返回音频的编码格式
            synthesizer.setFormat(OutputFormatEnum.WAV);
            //设置返回音频的采样率
            synthesizer.setSampleRate(SampleRateEnum.SAMPLE_RATE_16K);
            //发音人
            synthesizer.setVoice("siyue");
            //语调，范围是-500~500，可选，默认是0
            synthesizer.setPitchRate(100);
            //语速，范围是-500~500，默认是0
            synthesizer.setSpeechRate(100);
            //设置用于语音合成的文本
            synthesizer.setText(text);

            synthesizer.addCustomedParam("enable_subtitle", true);

            //此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            long start = System.currentTimeMillis();

            synthesizer.start();
            logger.info("tts start latency " + (System.currentTimeMillis() - start) + " ms");
            startTime = System.currentTimeMillis();
            //等待语音合成结束
            synthesizer.waitForComplete();
            logger.info("tts stop latency " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭连接
            if (null != synthesizer) {
                synthesizer.close();
            }
        }
    }
    private static SpeechSynthesizerListener getSynthesizerListener() {
        SpeechSynthesizerListener listener = null;
        try {
            listener = new SpeechSynthesizerListener() {
                File f=new File("tts_test.wav");
                FileOutputStream fout = new FileOutputStream(f);
                private boolean firstRecvBinary = true;

                //语音合成结束
                @Override
                public void onComplete(SpeechSynthesizerResponse response) {
                    // TODO 当onComplete时表示所有TTS数据已经接收完成，因此这个是整个合成延迟，该延迟可能较大，未必满足实时场景
                    System.out.println("name: " + response.getName() + ", status: " + response.getStatus()+", output file :"+f.getAbsolutePath());
                }

                //语音合成的语音二进制数据
                @Override
                public void onMessage(ByteBuffer message) {
                    try {
                        if(firstRecvBinary) {
                            // TODO 此处是计算首包语音流的延迟，收到第一包语音流时，即可以进行语音播放，以提升响应速度(特别是实时交互场景下)
                            firstRecvBinary = false;
                            long now = System.currentTimeMillis();
                            logger.info("tts first latency : " + (now - startTime) + " ms");
                        }
                        byte[] bytesArray = new byte[message.remaining()];
                        message.get(bytesArray, 0, bytesArray.length);
                        //System.out.println("write array:" + bytesArray.length);
                        fout.write(bytesArray);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFail(SpeechSynthesizerResponse response){
                    // TODO 重要提示： task_id很重要，是调用方和服务端通信的唯一ID标识，当遇到问题时，需要提供此task_id以便排查
                    System.out.println(
                            "task_id: " + response.getTaskId() +
                                    //状态码 20000000 表示识别成功
                                    ", status: " + response.getStatus() +
                                    //错误信息
                                    ", status_text: " + response.getStatusText());
                }

                @Override
                public void onMetaInfo(SpeechSynthesizerResponse response) {
                    System.out.println("MetaInfo event:{}" +  response.getTaskId());
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listener;
    }
}
