package com.academic.publications.catalog.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String COLA_PUBLICACIONES = "cola.publicaciones";

    @Bean
    public Queue colaPublicaciones() {
        return new Queue(COLA_PUBLICACIONES, true);
    }
}