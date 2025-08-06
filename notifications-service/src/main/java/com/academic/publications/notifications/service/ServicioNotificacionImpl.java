package com.academic.publications.notifications.service;

import com.academic.publications.notifications.dto.MensajeEmail;
import com.academic.publications.notifications.dto.NotificacionDTO;
import com.academic.publications.notifications.model.Notificacion;
import com.academic.publications.notifications.repository.NotificacionRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ServicioNotificacionImpl implements ServicioNotificacion {

    private final NotificacionRepositorio notificacionRepositorio;
    private final ServicioEmail servicioEmail;
    private final ServicioWebSocket servicioWebSocket;

    @Autowired
    public ServicioNotificacionImpl(NotificacionRepositorio notificacionRepositorio,
                                    ServicioEmail servicioEmail,
                                    ServicioWebSocket servicioWebSocket) {
        this.notificacionRepositorio = notificacionRepositorio;
        this.servicioEmail = servicioEmail;
        this.servicioWebSocket = servicioWebSocket;
    }

    @Override
    @Transactional
    public Notificacion registrarNotificacion(NotificacionDTO notificacionDTO) {
        Notificacion notificacion = new Notificacion();
        notificacion.setTipo(notificacionDTO.getTipo());
        notificacion.setDestinatario(notificacionDTO.getDestinatario());
        notificacion.setContenido(notificacionDTO.getContenido());
        notificacion.setFechaEnvio(new Date());
        notificacion.setEnviada(false);

        return notificacionRepositorio.save(notificacion);
    }

    @Override
    public void enviarNotificacion(Notificacion notificacion) {
        try {
            switch (notificacion.getTipo()) {
                case "EMAIL":
                    MensajeEmail mensajeEmail = new MensajeEmail();
                    mensajeEmail.setDestinatario(notificacion.getDestinatario());
                    mensajeEmail.setAsunto("Notificación del sistema");
                    mensajeEmail.setContenido(notificacion.getContenido());
                    servicioEmail.enviarEmail(mensajeEmail);
                    break;
                case "WEBSOCKET":
                    servicioWebSocket.enviarNotificacion(notificacion.getDestinatario(), notificacion.getContenido());
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de notificación no soportado");
            }

            notificacion.setEnviada(true);
            notificacionRepositorio.save(notificacion);
        } catch (Exception e) {
            System.err.println("Error enviando notificación: " + e.getMessage());
        }
    }
}