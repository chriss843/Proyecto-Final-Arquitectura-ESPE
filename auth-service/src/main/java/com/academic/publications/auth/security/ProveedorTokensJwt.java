package com.academic.publications.auth.security;

import com.academic.publications.auth.config.ConfiguracionJwt;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ProveedorTokensJwt {

    private final ConfiguracionJwt configuracionJwt;

    public ProveedorTokensJwt(ConfiguracionJwt configuracionJwt) {
        this.configuracionJwt = configuracionJwt;
    }

    public String generarTokenAcceso(Authentication authentication) {
        UserDetails usuarioPrincipal = (UserDetails) authentication.getPrincipal();
        return generarToken(usuarioPrincipal.getUsername(), configuracionJwt.getExpiracionToken());
    }

    public String generarTokenAcceso(UserDetails usuario) {
        return generarToken(usuario.getUsername(), configuracionJwt.getExpiracionToken());
    }

    public String generarTokenActualizacion(UserDetails usuario) {
        return generarToken(usuario.getUsername(), configuracionJwt.getExpiracionRefreshToken());
    }

    private String generarToken(String email, long expiracion) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiracion))
                .signWith(SignatureAlgorithm.HS512, configuracionJwt.getSecreto())
                .compact();
    }

    public boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(configuracionJwt.getSecreto()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.err.println("Error validando token JWT: " + e.getMessage());
        }
        return false;
    }

    public String obtenerEmailDeToken(String token) {
        return Jwts.parser()
                .setSigningKey(configuracionJwt.getSecreto())
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}