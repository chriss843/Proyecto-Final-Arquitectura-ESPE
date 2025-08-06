package com.academic.publications.notifications.service;

import com.academic.publications.notifications.dto.MensajeEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import jakarta.mail.internet.MimeMessage;

@Service
public class ServicioEmail {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    public ServicioEmail(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void enviarEmail(MensajeEmail mensaje) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(mensaje.getDestinatario());
            helper.setSubject(mensaje.getAsunto());
            helper.setText(mensaje.getContenido(), true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error enviando email", e);
        }
    }

    public String construirContenidoEmail(String plantilla, Context contexto) {
        return templateEngine.process(plantilla, contexto);
    }
}