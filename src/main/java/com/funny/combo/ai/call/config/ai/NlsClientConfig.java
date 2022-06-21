package com.funny.combo.ai.call.config.ai;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Slf4j
@Order(10)
@Configuration
public class NlsClientConfig {
    public final static String accessKeyId = "LTAI5tE1Dkg1P6pVtqMHUBDo";
    public final static String accessKeySecret = "Y552OEQQCJoBg1PiCOW6PmJXHgC3ef";
    public final static String appKey = "jIbYHpawmiuIi2IX";
    public final static String BEIJING_GATEWAY = "wss://nls-gateway-cn-beijing.aliyuncs.com/ws/v1";

    @Bean
    public AliClientFactory asrAliClientFactory() {
        AliClientFactory aliClientFactory = new AliClientFactory();
        aliClientFactory.setAccessKeyId(accessKeyId);
        aliClientFactory.setAccessKeySecret(accessKeySecret);
        return aliClientFactory;
    }

}
