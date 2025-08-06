package com.academic.publications.auth.controller;

import com.academic.publications.auth.dto.SolicitudRegistro;
import com.academic.publications.auth.model.Usuario;
import com.academic.publications.auth.service.ServicioUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class ControladorUsuario {

    private final ServicioUsuario servicioUsuario;

    @Autowired
    public ControladorUsuario(ServicioUsuario servicioUsuario) {
        this.servicioUsuario = servicioUsuario;
    }

    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody SolicitudRegistro solicitud) {
        Usuario usuario = servicioUsuario.registrarUsuario(solicitud);
        return ResponseEntity.ok(usuario);
    }
}