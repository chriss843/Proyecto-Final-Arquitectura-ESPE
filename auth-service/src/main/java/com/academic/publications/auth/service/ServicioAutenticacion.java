package com.academic.publications.auth.service;

import com.academic.publications.auth.dto.RespuestaToken;

public interface ServicioAutenticacion {
    RespuestaToken autenticarUsuario(String email, String password);
    RespuestaToken refrescarToken(String tokenActualizacion);
    void registrarAuditoria(String evento, String email);
}