package com.academic.publications.catalog.controller;

import com.academic.publications.catalog.dto.EntradaCatalogoDTO;
import com.academic.publications.catalog.model.EntradaCatalogo;
import com.academic.publications.catalog.service.ServicioCatalogo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/catalogo")
public class ControladorCatalogo {

    private final ServicioCatalogo servicioCatalogo;

    @Autowired
    public ControladorCatalogo(ServicioCatalogo servicioCatalogo) {
        this.servicioCatalogo = servicioCatalogo;
    }

    @PostMapping
    public ResponseEntity<EntradaCatalogo> crearEntrada(@RequestBody EntradaCatalogoDTO entradaDTO) {
        EntradaCatalogo entrada = servicioCatalogo.crearEntrada(entradaDTO);
        return ResponseEntity.ok(entrada);
    }

    @GetMapping
    public ResponseEntity<Page<EntradaCatalogo>> obtenerTodasLasEntradas(Pageable pageable) {
        Page<EntradaCatalogo> entradas = servicioCatalogo.obtenerTodasLasEntradas(pageable);
        return ResponseEntity.ok(entradas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntradaCatalogo> obtenerEntradaPorId(@PathVariable Long id) {
        EntradaCatalogo entrada = servicioCatalogo.obtenerEntradaPorId(id);
        return ResponseEntity.ok(entrada);
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<EntradaCatalogo>> buscarPorTermino(
            @RequestParam String termino,
            Pageable pageable) {
        Page<EntradaCatalogo> entradas = servicioCatalogo.buscarPorTermino(termino, pageable);
        return ResponseEntity.ok(entradas);
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<Page<EntradaCatalogo>> buscarPorTipo(
            @PathVariable String tipo,
            Pageable pageable) {
        Page<EntradaCatalogo> entradas = servicioCatalogo.buscarPorTipo(tipo, pageable);
        return ResponseEntity.ok(entradas);
    }

    @GetMapping("/identificador/{identificador}")
    public ResponseEntity<EntradaCatalogo> buscarPorIdentificador(
            @PathVariable String identificador) {
        EntradaCatalogo entrada = servicioCatalogo.buscarPorIdentificador(identificador);
        return ResponseEntity.ok(entrada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEntrada(@PathVariable Long id) {
        servicioCatalogo.eliminarEntrada(id);
        return ResponseEntity.noContent().build();
    }
}