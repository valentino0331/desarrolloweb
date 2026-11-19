package com.utp.ecotechtrack.controller;

import com.utp.ecotechtrack.dto.AlmacenDTO;
import com.utp.ecotechtrack.service.AlmacenService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de Almacenes y depósitos de custodia temporal de RAEE.
 * Protegido mediante Spring Security y RBAC.
 */
@RestController
@RequestMapping("/api/almacenes")
public class AlmacenController {

    private final AlmacenService almacenService;

    public AlmacenController(AlmacenService almacenService) {
        this.almacenService = almacenService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<List<AlmacenDTO>> listarTodos() {
        return ResponseEntity.ok(almacenService.listarTodos());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<AlmacenDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(almacenService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlmacenDTO> registrar(@Valid @RequestBody AlmacenDTO dto) {
        AlmacenDTO creado = almacenService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AlmacenDTO> actualizar(@PathVariable Long id, @Valid @RequestBody AlmacenDTO dto) {
        return ResponseEntity.ok(almacenService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        almacenService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
