package com.example.skipro.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Binds JWT settings from application.properties.
 */
@ConfigurationProperties(prefix = "app.jwt")
public class AppJwtProperties {
    /**
     * Base64-encoded secret for signing HS256 tokens.
     */
    private String secret;

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}

