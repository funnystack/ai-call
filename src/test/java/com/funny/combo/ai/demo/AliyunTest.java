package com.funny.combo.ai.demo;

import com.funny.combo.ai.demo.service.asr.AliyunASRService;
import com.funny.combo.ai.demo.service.asr.AliyunTTSService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AliyunTest {
    @Resource
    private AliyunASRService aliyunAsrService;
    @Resource
    private AliyunTTSService aliyunTTSService;
    @Test
    public void asr(){
        aliyunAsrService.asrProcess("/Users/fangli/github/aliyun-asr-tts/src/main/resources/nls-sample-16k.wav","123");
    }

    @Test
    public void tts(){
        aliyunTTSService.ttsProcess("天气好大家出来玩吧");
    }

}
