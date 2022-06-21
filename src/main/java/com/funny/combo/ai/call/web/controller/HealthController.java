package com.funny.combo.ai.call.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
public class HealthController {
    @GetMapping("/health/status")
    public String health() {
        return "ok";
    }
}
