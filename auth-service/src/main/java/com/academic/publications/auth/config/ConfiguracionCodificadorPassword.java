package com.academic.publications.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ConfiguracionCodificadorPassword {

    @Bean
    public BCryptPasswordEncoder codificadorPassword() {
        return new BCryptPasswordEncoder();
    }
}