package com.funny.combo.ai.call;

import com.alibaba.fastjson.JSON;
import com.funny.combo.ai.call.common.BaseResult;
import com.funny.combo.ai.call.service.asr.AsrRequest;
import com.funny.combo.ai.call.service.asr.AsrService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AliyunTest {
    @Resource
    private AsrService asrService;

    @Test
    public void asr(){
        AsrRequest asrRequest = new AsrRequest();
        asrRequest.setFilePath("/Users/fangli/github/ai-call/radio/10.pcm");
        asrRequest.setSessionId("123");
        asrRequest.setExtParam("ext");
        BaseResult baseResult =  asrService.asr(asrRequest);
        System.out.println(JSON.toJSONString(baseResult));
    }

}
