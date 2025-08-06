package com.academic.publications.auth.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionRabbitMQ {

    public static final String INTERCAMBIO_AUDITORIA = "auditoria.eventos";

    @Bean
    public TopicExchange intercambioAuditoria() {
        return new TopicExchange(INTERCAMBIO_AUDITORIA);
    }
}