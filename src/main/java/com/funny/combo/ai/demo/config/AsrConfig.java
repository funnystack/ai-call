package com.funny.combo.ai.demo.config;

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.NlsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AsrConfig {
    private static String accessKeyId = "LTAI5tE1Dkg1P6pVtqMHUBDo";
    private static String accessKeySecret = "Y552OEQQCJoBg1PiCOW6PmJXHgC3ef";

    @Bean
    public NlsClient nlsClient() {
        // 重要提示 创建NlsClient实例,应用全局创建一个即可,生命周期可和整个应用保持一致,默认服务地址为阿里云线上服务地址
        NlsClient client = null;
        try {
            System.out.println("accessKeyId=" + accessKeyId + "; accessKeySecret=" + accessKeySecret);
            AccessToken accessToken = new AccessToken(accessKeyId, accessKeySecret);
            accessToken.apply();
            System.out.println("Token: " + accessToken.getToken() + ", expire time: " + accessToken.getExpireTime());
            client = new NlsClient(accessToken.getToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return client;
    }


}
