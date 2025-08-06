package com.academic.publications.auth.service;

import com.academic.publications.auth.config.ConfiguracionJwt;
import com.academic.publications.auth.dto.RespuestaToken;
import com.academic.publications.auth.event.PublicadorEventosAuditoria;
import com.academic.publications.auth.model.TokenActualizacion;
import com.academic.publications.auth.model.Usuario;
import com.academic.publications.auth.repository.TokenActualizacionRepositorio;
import com.academic.publications.auth.repository.UsuarioRepositorio;
import com.academic.publications.auth.security.ProveedorTokensJwt;
import com.academic.publications.auth.security.ServicioDetallesUsuario;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class ServicioAutenticacionImpl implements ServicioAutenticacion {

    private final AuthenticationManager administradorAutenticacion;
    private final ProveedorTokensJwt proveedorTokens;
    private final ServicioDetallesUsuario servicioDetallesUsuario;
    private final TokenActualizacionRepositorio tokenActualizacionRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private final PublicadorEventosAuditoria publicadorAuditoria;
    private final ConfiguracionJwt configuracionJwt;

    public ServicioAutenticacionImpl(AuthenticationManager administradorAutenticacion,
                                     ProveedorTokensJwt proveedorTokens,
                                     ConfiguracionJwt configuracionJwt,
                                     ServicioDetallesUsuario servicioDetallesUsuario,
                                     TokenActualizacionRepositorio tokenActualizacionRepositorio,
                                     UsuarioRepositorio usuarioRepositorio,
                                     PublicadorEventosAuditoria publicadorAuditoria) {
        this.administradorAutenticacion = administradorAutenticacion;
        this.proveedorTokens = proveedorTokens;
        this.servicioDetallesUsuario = servicioDetallesUsuario;
        this.configuracionJwt = configuracionJwt;
        this.tokenActualizacionRepositorio = tokenActualizacionRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
        this.publicadorAuditoria = publicadorAuditoria;
    }

    @Override
    public RespuestaToken autenticarUsuario(String email, String password) {
        Authentication autenticacion = administradorAutenticacion.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(autenticacion);

        UserDetails usuario = (UserDetails) autenticacion.getPrincipal();
        String tokenAcceso = proveedorTokens.generarTokenAcceso(usuario);
        String tokenActualizacion = proveedorTokens.generarTokenActualizacion(usuario);

        guardarTokenActualizacion(usuario.getUsername(), tokenActualizacion);

        registrarAuditoria("LOGIN", email);

        RespuestaToken respuesta = new RespuestaToken();
        respuesta.setTokenAcceso(tokenAcceso);
        respuesta.setTokenActualizacion(tokenActualizacion);

        return respuesta;
    }

    @Override
    public RespuestaToken refrescarToken(String tokenActualizacion) {
        if (!proveedorTokens.validarToken(tokenActualizacion)) {
            throw new RuntimeException("Token de actualización inválido");
        }

        String email = proveedorTokens.obtenerEmailDeToken(tokenActualizacion);
        UserDetails usuario = servicioDetallesUsuario.loadUserByUsername(email);

        String nuevoTokenAcceso = proveedorTokens.generarTokenAcceso(usuario);
        String nuevoTokenActualizacion = proveedorTokens.generarTokenActualizacion(usuario);

        guardarTokenActualizacion(email, nuevoTokenActualizacion);

        RespuestaToken respuesta = new RespuestaToken();
        respuesta.setTokenAcceso(nuevoTokenAcceso);
        respuesta.setTokenActualizacion(nuevoTokenActualizacion);

        return respuesta;
    }

    @Override
    public void registrarAuditoria(String evento, String email) {
        publicadorAuditoria.publicarEventoAuditoria(evento, email);
    }

    @Transactional
    private void guardarTokenActualizacion(String email, String token) {
        Usuario usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TokenActualizacion tokenActualizacion = new TokenActualizacion();
        tokenActualizacion.setUsuario(usuario);
        tokenActualizacion.setToken(token);
        tokenActualizacion.setFechaExpiracion(
                new Date(System.currentTimeMillis() + configuracionJwt.getExpiracionRefreshToken())
        );

        tokenActualizacionRepositorio.deleteByUsuarioId(usuario.getId());
        tokenActualizacionRepositorio.save(tokenActualizacion);
    }
}