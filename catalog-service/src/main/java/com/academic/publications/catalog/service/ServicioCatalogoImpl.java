package com.academic.publications.catalog.service;

import com.academic.publications.catalog.dto.EntradaCatalogoDTO;
import com.academic.publications.catalog.model.EntradaCatalogo;
import com.academic.publications.catalog.repository.CatalogoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicioCatalogoImpl implements ServicioCatalogo {

    private final CatalogoRepositorio catalogoRepositorio;

    @Autowired
    public ServicioCatalogoImpl(CatalogoRepositorio catalogoRepositorio) {
        this.catalogoRepositorio = catalogoRepositorio;
    }

    @Override
    @Transactional
    public EntradaCatalogo crearEntrada(EntradaCatalogoDTO entradaDTO) {
        EntradaCatalogo entrada = new EntradaCatalogo();
        mapearDTOaEntidad(entradaDTO, entrada);
        return catalogoRepositorio.save(entrada);
    }

    @Override
    public Page<EntradaCatalogo> obtenerTodasLasEntradas(Pageable pageable) {
        return catalogoRepositorio.findAll(pageable);
    }

    @Override
    public EntradaCatalogo obtenerEntradaPorId(Long id) {
        return catalogoRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada"));
    }

    @Override
    public Page<EntradaCatalogo> buscarPorTermino(String termino, Pageable pageable) {
        return catalogoRepositorio.buscarPorTermino(termino, pageable);
    }

    @Override
    public Page<EntradaCatalogo> buscarPorTipo(String tipo, Pageable pageable) {
        return catalogoRepositorio.findByTipoPublicacion(tipo, pageable);
    }

    @Override
    public EntradaCatalogo buscarPorIdentificador(String identificador) {
        return catalogoRepositorio.findByIdentificador(identificador)
                .orElseThrow(() -> new RuntimeException("Entrada no encontrada para el identificador: " + identificador));
    }

    @Override
    public void eliminarEntrada(Long id) {
        catalogoRepositorio.deleteById(id);
    }

    private void mapearDTOaEntidad(EntradaCatalogoDTO dto, EntradaCatalogo entrada) {
        entrada.setTitulo(dto.getTitulo());
        entrada.setResumen(dto.getResumen());
        entrada.setTipoPublicacion(dto.getTipoPublicacion());
        entrada.setAutores(dto.getAutores());
        entrada.setFechaPublicacion(dto.getFechaPublicacion());
        entrada.setIdentificador(dto.getIdentificador());
        entrada.setPalabrasClave(dto.getPalabrasClave());
        entrada.setCategorias(dto.getCategorias());
    }
}