package com.academic.publications.notifications.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class ServicioWebSocket {

    private final SimpMessagingTemplate messagingTemplate;

    public ServicioWebSocket(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void enviarNotificacion(String usuario, String mensaje) {
        messagingTemplate.convertAndSendToUser(
                usuario,
                "/queue/notificaciones",
                mensaje
        );
    }
}