package com.academic.publications.catalog.repository;

import com.academic.publications.catalog.model.EntradaCatalogo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatalogoRepositorio extends JpaRepository<EntradaCatalogo, Long> {

    @Query("SELECT e FROM EntradaCatalogo e WHERE " +
            "LOWER(e.titulo) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(e.autores) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +
            "LOWER(e.palabrasClave) LIKE LOWER(CONCAT('%', :termino, '%'))")
    Page<EntradaCatalogo> buscarPorTermino(@Param("termino") String termino, Pageable pageable);

    Page<EntradaCatalogo> findByTipoPublicacion(String tipoPublicacion, Pageable pageable);

    Optional<EntradaCatalogo> findByIdentificador(String identificador);
}