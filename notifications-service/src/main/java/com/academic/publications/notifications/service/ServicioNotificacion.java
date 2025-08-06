package com.academic.publications.notifications.service;

import com.academic.publications.notifications.dto.NotificacionDTO;
import com.academic.publications.notifications.model.Notificacion;

public interface ServicioNotificacion {
    Notificacion registrarNotificacion(NotificacionDTO notificacionDTO);
    void enviarNotificacion(Notificacion notificacion);
}