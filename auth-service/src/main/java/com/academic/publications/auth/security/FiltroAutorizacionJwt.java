package com.academic.publications.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class FiltroAutorizacionJwt extends OncePerRequestFilter {

    private final ProveedorTokensJwt proveedorTokens;
    private final ServicioDetallesUsuario servicioDetallesUsuario;

    public FiltroAutorizacionJwt(ProveedorTokensJwt proveedorTokens,
                                 ServicioDetallesUsuario servicioDetallesUsuario) {
        this.proveedorTokens = proveedorTokens;
        this.servicioDetallesUsuario = servicioDetallesUsuario;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String token = extraerToken(request);

            if (token != null && proveedorTokens.validarToken(token)) {
                String email = proveedorTokens.obtenerEmailDeToken(token);

                UserDetails detallesUsuario = servicioDetallesUsuario.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken autenticacion =
                        new UsernamePasswordAuthenticationToken(
                                detallesUsuario,
                                null,
                                detallesUsuario.getAuthorities()
                        );
                autenticacion.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
        } catch (Exception e) {
            System.err.println("No se pudo establecer autenticación: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String extraerToken(HttpServletRequest request) {
        String cabecera = request.getHeader("Authorization");

        if (cabecera != null && cabecera.startsWith("Bearer ")) {
            return cabecera.substring(7);
        }
        return null;
    }
}