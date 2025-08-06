package com.academic.publications.auth.controller;

import com.academic.publications.auth.dto.RespuestaToken;
import com.academic.publications.auth.dto.SolicitudLogin;
import com.academic.publications.auth.service.ServicioAutenticacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/auth")
public class ControladorAutenticacion {

    private final ServicioAutenticacion servicioAutenticacion;

    @Autowired
    public ControladorAutenticacion(ServicioAutenticacion servicioAutenticacion) {
        this.servicioAutenticacion = servicioAutenticacion;
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaToken> login(@RequestBody SolicitudLogin solicitud) {
        RespuestaToken respuesta = servicioAutenticacion.autenticarUsuario(
                solicitud.getEmail(),
                solicitud.getPassword()
        );
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/refrescar")
    public ResponseEntity<RespuestaToken> refrescarToken(@RequestParam String token) {
        RespuestaToken respuesta = servicioAutenticacion.refrescarToken(token);
        return ResponseEntity.ok(respuesta);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion != null) {
            servicioAutenticacion.registrarAuditoria("LOGOUT", autenticacion.getName());
        }
        return ResponseEntity.noContent().build();
    }
}