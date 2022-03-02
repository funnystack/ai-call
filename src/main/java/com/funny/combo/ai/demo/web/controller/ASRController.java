package com.funny.combo.ai.demo.web.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 枚举信息接口
 */
@Slf4j
@Controller
public class ASRController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }

}
