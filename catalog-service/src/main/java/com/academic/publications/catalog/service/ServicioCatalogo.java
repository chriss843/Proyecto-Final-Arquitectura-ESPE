package com.academic.publications.catalog.service;

import com.academic.publications.catalog.dto.EntradaCatalogoDTO;
import com.academic.publications.catalog.model.EntradaCatalogo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ServicioCatalogo {
    EntradaCatalogo crearEntrada(EntradaCatalogoDTO entradaDTO);
    Page<EntradaCatalogo> obtenerTodasLasEntradas(Pageable pageable);
    EntradaCatalogo obtenerEntradaPorId(Long id);
    Page<EntradaCatalogo> buscarPorTermino(String termino, Pageable pageable);
    Page<EntradaCatalogo> buscarPorTipo(String tipo, Pageable pageable);
    EntradaCatalogo buscarPorIdentificador(String identificador);
    void eliminarEntrada(Long id);
}