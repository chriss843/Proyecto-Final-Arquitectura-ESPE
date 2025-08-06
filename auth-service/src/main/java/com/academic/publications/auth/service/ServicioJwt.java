package com.academic.publications.auth.service;

import com.academic.publications.auth.security.ProveedorTokensJwt;
import org.springframework.stereotype.Service;

@Service
public class ServicioJwt {

    private final ProveedorTokensJwt proveedorTokens;

    public ServicioJwt(ProveedorTokensJwt proveedorTokens) {
        this.proveedorTokens = proveedorTokens;
    }

    public String extraerEmailDeToken(String token) {
        return proveedorTokens.obtenerEmailDeToken(token);
    }

    public boolean validarToken(String token) {
        return proveedorTokens.validarToken(token);
    }
}