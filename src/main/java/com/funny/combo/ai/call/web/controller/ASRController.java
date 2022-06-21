package com.funny.combo.ai.call.web.controller;


import com.funny.combo.ai.call.common.BaseResult;
import com.funny.combo.ai.call.service.asr.AliASRService;
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
    private AliASRService aliASRService;

    @RequestMapping("/asr")
    public BaseResult asr(String path){
        aliASRService.asrProcess(path,"1");
        return BaseResult.OK();
    }

}
