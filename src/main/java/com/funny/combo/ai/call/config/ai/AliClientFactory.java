package com.funny.combo.ai.call.config.ai;

import com.alibaba.nls.client.AccessToken;
import com.alibaba.nls.client.protocol.NlsClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.Objects;

import static com.funny.combo.ai.call.config.ai.NlsClientConfig.BEIJING_GATEWAY;

/**
 * @Classname AliAsrClientFactory
 * @Description client工厂
 * @Date 2022/5/30 14:36
 * @Created by fangli
 */
@Slf4j
@Data
public class AliClientFactory {
    private String accessKeyId;
    private String accessKeySecret;

    private volatile NlsClient nlsClient = null;

    public NlsClient getNlsClient() {
        NlsClient client = nlsClient;
        if (Objects.isNull(client)) {
            synchronized (this) {
                if (Objects.isNull(nlsClient)) {
                    nlsClient = createNlsClient();
                    client = nlsClient;
                }
            }
        }
        return client;
    }

    /**
     * 重置刷新Token
     */
    public synchronized void reset() {
        nlsClient = null;
    }

    /**
     * 每天晚上1点，重置一次
     */
    @Scheduled(cron = "0 40 23 * * ?")
    public void refresh() {
        log.info("刷新了AliNlsClient");
        this.reset();
    }


    private NlsClient createNlsClient() {
        // 重要提示 创建NlsClient实例,应用全局创建一个即可,生命周期可和整个应用保持一致,默认服务地址为阿里云线上服务地址
        try {
            log.info("accessKeyId=" + accessKeyId + "; accessKeySecret=" + accessKeySecret);
            AccessToken accessToken = new AccessToken(accessKeyId, accessKeySecret);
            accessToken.apply();
            log.info("Token: " + accessToken.getToken() + ", expire time: " + accessToken.getExpireTime());
            return new NlsClient(BEIJING_GATEWAY, accessToken.getToken());
        } catch (IOException e) {
            log.error("创建Nsl client异常", e);
            return null;
        }

    }
}
