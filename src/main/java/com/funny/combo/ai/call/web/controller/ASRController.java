package com.funny.combo.ai.call.web.controller;


import com.funny.combo.ai.call.common.BaseResult;
import com.funny.combo.ai.call.service.asr.AsrRequest;
import com.funny.combo.ai.call.service.asr.AsrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 */
@Slf4j
@RestController
public class ASRController {
    @Resource
    private AsrService asrService;

    @RequestMapping("/asr")
    public BaseResult asr(String filePath){
        AsrRequest asrRequest = new AsrRequest();
        asrRequest.setFilePath("/Users/fangli/github/ai-call/radio/10.pcm");
        asrRequest.setSessionId("123");
        asrRequest.setExtParam("ext");
        return asrService.asr(asrRequest);
    }

}
