package com.utp.ecotechtrack.controller;

import com.utp.ecotechtrack.dto.EmpresaGestoraDTO;
import com.utp.ecotechtrack.service.EmpresaGestoraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la administración de Empresas Gestoras / Operadoras RAEE autorizadas.
 * Protegido mediante Spring Security y RBAC.
 */
@RestController
@RequestMapping("/api/empresas-gestoras")
public class EmpresaGestoraController {

    private final EmpresaGestoraService empresaGestoraService;

    public EmpresaGestoraController(EmpresaGestoraService empresaGestoraService) {
        this.empresaGestoraService = empresaGestoraService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<List<EmpresaGestoraDTO>> listarTodas() {
        return ResponseEntity.ok(empresaGestoraService.listarTodas());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<EmpresaGestoraDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(empresaGestoraService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpresaGestoraDTO> registrar(@Valid @RequestBody EmpresaGestoraDTO dto) {
        EmpresaGestoraDTO creada = empresaGestoraService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmpresaGestoraDTO> actualizar(@PathVariable Long id, @Valid @RequestBody EmpresaGestoraDTO dto) {
        return ResponseEntity.ok(empresaGestoraService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        empresaGestoraService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
