package com.academic.publications.auth.service;

import com.academic.publications.auth.dto.SolicitudRegistro;
import com.academic.publications.auth.event.PublicadorEventosAuditoria;
import com.academic.publications.auth.model.Usuario;
import com.academic.publications.auth.model.Rol;
import com.academic.publications.auth.repository.UsuarioRepositorio;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioUsuario {

    private final UsuarioRepositorio usuarioRepositorio;
    private final PasswordEncoder codificadorPassword;
    private final PublicadorEventosAuditoria publicadorAuditoria;

    public ServicioUsuario(UsuarioRepositorio usuarioRepositorio,
                           PasswordEncoder codificadorPassword,
                           PublicadorEventosAuditoria publicadorAuditoria) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.codificadorPassword = codificadorPassword;
        this.publicadorAuditoria = publicadorAuditoria;
    }

    @Transactional
    public Usuario registrarUsuario(SolicitudRegistro solicitud) {
        if (usuarioRepositorio.existsByEmail(solicitud.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(solicitud.getNombre());
        usuario.setApellido(solicitud.getApellido());
        usuario.setEmail(solicitud.getEmail());
        usuario.setPassword(codificadorPassword.encode(solicitud.getPassword()));
        usuario.setRol(Rol.valueOf(solicitud.getRol().toUpperCase()));
        usuario.setHabilitado(true);

        Usuario usuarioGuardado = usuarioRepositorio.save(usuario);

        publicadorAuditoria.publicarEventoAuditoria("REGISTRO", usuario.getEmail());

        return usuarioGuardado;
    }
}