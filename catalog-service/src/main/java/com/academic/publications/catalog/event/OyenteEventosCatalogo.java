package com.academic.publications.catalog.event;

import com.academic.publications.catalog.config.RabbitMQConfig;
import com.academic.publications.catalog.dto.EntradaCatalogoDTO;
import com.academic.publications.catalog.model.EntradaCatalogo;
import com.academic.publications.catalog.service.ServicioCatalogo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OyenteEventosCatalogo {

    private final ServicioCatalogo servicioCatalogo;
    private final ObjectMapper objectMapper;

    @Autowired
    public OyenteEventosCatalogo(ServicioCatalogo servicioCatalogo, ObjectMapper objectMapper) {
        this.servicioCatalogo = servicioCatalogo;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.COLA_PUBLICACIONES)
    public void manejarEventoPublicacion(String mensaje) {
        try {
            EventoPublicacion evento = objectMapper.readValue(mensaje, EventoPublicacion.class);

            if ("PUBLICACION_APROBADA".equals(evento.getTipoEvento())) {
                // Aquí procesarías el evento para crear una nueva entrada en el catálogo
                EntradaCatalogoDTO entradaDTO = new EntradaCatalogoDTO();
                entradaDTO.setTitulo(evento.getTitulo());
                entradaDTO.setTipoPublicacion(evento.getTipoPublicacion());
                entradaDTO.setAutores(String.join(", ", evento.getAutores()));
                entradaDTO.setFechaPublicacion(new java.util.Date());
                entradaDTO.setIdentificador(evento.getIdentificador());

                servicioCatalogo.crearEntrada(entradaDTO);
            }
        } catch (Exception e) {
            System.err.println("Error procesando evento: " + e.getMessage());
        }
    }

    public static class EventoPublicacion {
        private String tipoEvento;
        private String titulo;
        private String tipoPublicacion;
        private String[] autores;
        private String identificador; // DOI o ISBN

        // Getters y Setters
        public String getTipoEvento() {
            return tipoEvento;
        }

        public void setTipoEvento(String tipoEvento) {
            this.tipoEvento = tipoEvento;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getTipoPublicacion() {
            return tipoPublicacion;
        }

        public void setTipoPublicacion(String tipoPublicacion) {
            this.tipoPublicacion = tipoPublicacion;
        }

        public String[] getAutores() {
            return autores;
        }

        public void setAutores(String[] autores) {
            this.autores = autores;
        }

        public String getIdentificador() {
            return identificador;
        }

        public void setIdentificador(String identificador) {
            this.identificador = identificador;
        }
    }
}