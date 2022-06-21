package com.funny.combo.ai.call;

import com.funny.combo.ai.call.service.asr.AliASRService;
import com.funny.combo.ai.call.service.asr.AliTTSService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AliyunTest {
    @Resource
    private AliASRService aliAsrService;
    @Resource
    private AliTTSService aliTTSService;
    @Test
    public void asr(){
        aliAsrService.asrProcess("/Users/fangli/github/aliyun-asr-tts/src/main/resources/nls-sample-16k.wav","123");
    }

    @Test
    public void tts(){
        aliTTSService.ttsProcess("天气好大家出来玩吧");
    }

}
