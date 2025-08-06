package com.academic.publications.notifications.event;

import com.academic.publications.notifications.config.RabbitMQConfig;
import com.academic.publications.notifications.dto.NotificacionDTO;
import com.academic.publications.notifications.model.Notificacion;
import com.academic.publications.notifications.service.ServicioNotificacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OyenteEventosNotificacion {

    private final ServicioNotificacion servicioNotificacion;
    private final ObjectMapper objectMapper;

    @Autowired
    public OyenteEventosNotificacion(ServicioNotificacion servicioNotificacion,
                                     ObjectMapper objectMapper) {
        this.servicioNotificacion = servicioNotificacion;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.COLA_NOTIFICACIONES)
    public void manejarEventoNotificacion(String mensaje) {
        try {
            EventoNotificacion evento = objectMapper.readValue(mensaje, EventoNotificacion.class);

            NotificacionDTO notificacionDTO = new NotificacionDTO();
            notificacionDTO.setTipo(evento.getTipo());
            notificacionDTO.setDestinatario(evento.getDestinatario());
            notificacionDTO.setContenido(evento.getContenido());

            Notificacion notificacion = servicioNotificacion.registrarNotificacion(notificacionDTO);
            servicioNotificacion.enviarNotificacion(notificacion);
        } catch (Exception e) {
            System.err.println("Error procesando evento de notificación: " + e.getMessage());
        }
    }

    public static class EventoNotificacion {
        private String tipo;
        private String destinatario;
        private String contenido;

        // Getters y Setters
        public String getTipo() {
            return tipo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getDestinatario() {
            return destinatario;
        }

        public void setDestinatario(String destinatario) {
            this.destinatario = destinatario;
        }

        public String getContenido() {
            return contenido;
        }

        public void setContenido(String contenido) {
            this.contenido = contenido;
        }
    }
}