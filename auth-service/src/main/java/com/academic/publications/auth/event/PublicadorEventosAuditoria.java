package com.academic.publications.auth.event;

import com.academic.publications.auth.config.ConfiguracionRabbitMQ;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PublicadorEventosAuditoria {

    private final RabbitTemplate plantillaRabbit;

    @Autowired
    public PublicadorEventosAuditoria(RabbitTemplate plantillaRabbit) {
        this.plantillaRabbit = plantillaRabbit;
    }

    public void publicarEventoAuditoria(String tipoEvento, String emailUsuario) {
        EventoAuditoria evento = new EventoAuditoria();
        evento.setTipoEvento(tipoEvento);
        evento.setEmailUsuario(emailUsuario);
        evento.setFechaEvento(new Date());

        plantillaRabbit.convertAndSend(
                ConfiguracionRabbitMQ.INTERCAMBIO_AUDITORIA,
                "auditoria." + tipoEvento.toLowerCase(),
                evento
        );
    }
}