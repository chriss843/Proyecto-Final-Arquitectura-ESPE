package com.academic.publications.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class ConfiguracionJwt {

    private String secreto;
    private long expiracionToken;
    private long expiracionRefreshToken;

    // Getters y Setters
    public String getSecreto() {
        return secreto;
    }

    public void setSecreto(String secreto) {
        this.secreto = secreto;
    }

    public long getExpiracionToken() {
        return expiracionToken;
    }

    public void setExpiracionToken(long expiracionToken) {
        this.expiracionToken = expiracionToken;
    }

    public long getExpiracionRefreshToken() {
        return expiracionRefreshToken;
    }

    public void setExpiracionRefreshToken(long expiracionRefreshToken) {
        this.expiracionRefreshToken = expiracionRefreshToken;
    }
}