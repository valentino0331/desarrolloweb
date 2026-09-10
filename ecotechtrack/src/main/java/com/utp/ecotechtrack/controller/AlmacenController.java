package com.utp.ecotechtrack.controller;

import com.utp.ecotechtrack.dto.AlmacenDTO;
import com.utp.ecotechtrack.service.AlmacenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de Almacenes y depósitos de custodia temporal de RAEE.
 */
@RestController
@RequestMapping("/api/almacenes")
@CrossOrigin(origins = "*")
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @GetMapping
    public ResponseEntity<List<AlmacenDTO>> listarTodos() {
        return ResponseEntity.ok(almacenService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlmacenDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(almacenService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<AlmacenDTO> registrar(@Valid @RequestBody AlmacenDTO dto) {
        AlmacenDTO creado = almacenService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlmacenDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AlmacenDTO dto) {
        return ResponseEntity.ok(almacenService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        almacenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
