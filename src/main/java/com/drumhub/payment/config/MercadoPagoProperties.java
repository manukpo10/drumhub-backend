package com.drumhub.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app.mp")
public class MercadoPagoProperties {

    private String accessToken;
    private String webhookSecret;
    private String publicKey;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }

    public void setWebhookSecret(String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
