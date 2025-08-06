package com.academic.publications.auth.dto;

public class RespuestaToken {

    private String tokenAcceso;
    private String tokenActualizacion;

    // Getters y Setters
    public String getTokenAcceso() {
        return tokenAcceso;
    }

    public void setTokenAcceso(String tokenAcceso) {
        this.tokenAcceso = tokenAcceso;
    }

    public String getTokenActualizacion() {
        return tokenActualizacion;
    }

    public void setTokenActualizacion(String tokenActualizacion) {
        this.tokenActualizacion = tokenActualizacion;
    }
}