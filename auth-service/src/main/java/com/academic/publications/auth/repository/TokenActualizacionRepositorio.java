package com.academic.publications.auth.repository;

import com.academic.publications.auth.model.TokenActualizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenActualizacionRepositorio extends JpaRepository<TokenActualizacion, Long> {
    Optional<TokenActualizacion> findByToken(String token);
    void deleteByUsuarioId(Long usuarioId);
}