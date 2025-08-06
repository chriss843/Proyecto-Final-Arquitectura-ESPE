package com.academic.publications.auth.security;

import com.academic.publications.auth.dto.RespuestaToken;
import com.academic.publications.auth.dto.SolicitudLogin;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class FiltroAutenticacionJwt extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager administradorAutenticacion;
    private final ProveedorTokensJwt proveedorTokens;

    public FiltroAutenticacionJwt(AuthenticationManager administradorAutenticacion,
                                  ProveedorTokensJwt proveedorTokens) {
        this.administradorAutenticacion = administradorAutenticacion;
        this.proveedorTokens = proveedorTokens;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        try {
            SolicitudLogin credenciales = new ObjectMapper()
                    .readValue(request.getInputStream(), SolicitudLogin.class);

            UsernamePasswordAuthenticationToken tokenAutenticacion =
                    new UsernamePasswordAuthenticationToken(
                            credenciales.getEmail(),
                            credenciales.getPassword()
                    );

            return administradorAutenticacion.authenticate(tokenAutenticacion);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {
        UserDetails usuario = (UserDetails) authResult.getPrincipal();

        String tokenAcceso = proveedorTokens.generarTokenAcceso(usuario);
        String tokenActualizacion = proveedorTokens.generarTokenActualizacion(usuario);

        RespuestaToken respuestaToken = new RespuestaToken();
        respuestaToken.setTokenAcceso(tokenAcceso);
        respuestaToken.setTokenActualizacion(tokenActualizacion);

        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), respuestaToken);
    }
}