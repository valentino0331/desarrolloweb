package com.utp.ecotechtrack.controller;

import com.utp.ecotechtrack.dto.CambioEstadoDTO;
import com.utp.ecotechtrack.dto.EstadisticaEstadoDTO;
import com.utp.ecotechtrack.dto.HistorialMovimientoDTO;
import com.utp.ecotechtrack.dto.ResiduoRequestDTO;
import com.utp.ecotechtrack.dto.ResiduoResponseDTO;
import com.utp.ecotechtrack.model.CategoriaRAEE;
import com.utp.ecotechtrack.model.EstadoResiduo;
import com.utp.ecotechtrack.service.ResiduoService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para la gestión y trazabilidad de Residuos Electrónicos (RAEE).
 * Protegido mediante Spring Security y anotaciones @PreAuthorize con roles ADMIN, OPERADOR y USER.
 */
@RestController
@RequestMapping("/api/residuos")
public class ResiduoController {

    private final ResiduoService residuoService;

    public ResiduoController(ResiduoService residuoService) {
        this.residuoService = residuoService;
    }

    /**
     * Listar todos los residuos con filtros opcionales.
     * Accesible por ADMIN, OPERADOR y USER.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<List<ResiduoResponseDTO>> listarResiduos(
            @RequestParam(required = false) EstadoResiduo estado,
            @RequestParam(required = false) CategoriaRAEE categoria) {

        if (estado != null) {
            return ResponseEntity.ok(residuoService.listarPorEstado(estado));
        }
        if (categoria != null) {
            return ResponseEntity.ok(residuoService.listarPorCategoria(categoria));
        }
        return ResponseEntity.ok(residuoService.listarTodos());
    }

    /**
     * Obtener residuo por ID.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<ResiduoResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(residuoService.obtenerPorId(id));
    }

    /**
     * Consultar residuo por código único.
     */
    @GetMapping("/codigo/{codigo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<ResiduoResponseDTO> obtenerPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(residuoService.obtenerPorCodigo(codigo));
    }

    /**
     * Consultar historial de trazabilidad.
     */
    @GetMapping("/{id}/historial")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<List<HistorialMovimientoDTO>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(residuoService.obtenerHistorial(id));
    }

    /**
     * Registrar nuevo residuo.
     * Restringido a ADMIN y OPERADOR.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<ResiduoResponseDTO> registrarResiduo(@Valid @RequestBody ResiduoRequestDTO requestDTO) {
        ResiduoResponseDTO nuevoResiduo = residuoService.registrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoResiduo);
    }

    /**
     * Actualizar datos del residuo.
     * Restringido a ADMIN y OPERADOR.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<ResiduoResponseDTO> actualizarResiduo(
            @PathVariable Long id,
            @Valid @RequestBody ResiduoRequestDTO requestDTO) {
        ResiduoResponseDTO actualizado = residuoService.actualizar(id, requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Avanzar o cambiar estado en la trazabilidad.
     * Restringido a ADMIN y OPERADOR.
     */
    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR')")
    public ResponseEntity<ResiduoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoDTO cambioEstadoDTO) {
        ResiduoResponseDTO actualizado = residuoService.cambiarEstado(id, cambioEstadoDTO);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Eliminar físicamente un residuo.
     * Restringido exclusivamente al rol ADMIN.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarResiduo(@PathVariable Long id) {
        residuoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // =========================================================================
    // ENDPOINTS DE CONSULTAS PERSONALIZADAS JPQL (Requisito APF2)
    // =========================================================================

    /**
     * Consulta JPQL 1: Búsqueda de residuos dentro de un rango de fechas de registro.
     * GET /api/residuos/jpql/fechas?inicio=2026-01-01T00:00:00&fin=2026-12-31T23:59:59
     */
    @GetMapping("/jpql/fechas")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<List<ResiduoResponseDTO>> buscarPorRangoFechas(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(residuoService.buscarPorRangoFechas(inicio, fin));
    }

    /**
     * Consulta JPQL 2: Búsqueda multicriterio avanzada (estado, categoría y texto libre).
     * GET /api/residuos/jpql/busqueda-avanzada?estado=...&categoria=...&termino=...
     */
    @GetMapping("/jpql/busqueda-avanzada")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<List<ResiduoResponseDTO>> busquedaAvanzada(
            @RequestParam(required = false) EstadoResiduo estado,
            @RequestParam(required = false) CategoriaRAEE categoria,
            @RequestParam(required = false) String termino) {
        return ResponseEntity.ok(residuoService.busquedaAvanzada(estado, categoria, termino));
    }

    /**
     * Consulta JPQL 3: Métricas y agregaciones estadísticas por estado (conteo y suma de kg).
     * GET /api/residuos/jpql/estadisticas
     */
    @GetMapping("/jpql/estadisticas")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<List<EstadisticaEstadoDTO>> obtenerEstadisticas() {
        return ResponseEntity.ok(residuoService.obtenerEstadisticasPorEstado());
    }

    /**
     * Consulta JPQL 4: Filtrar residuos pesados en un almacén.
     * GET /api/residuos/jpql/almacen/{almacenId}/pesados?pesoMinimo=20.0
     */
    @GetMapping("/jpql/almacen/{almacenId}/pesados")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<List<ResiduoResponseDTO>> buscarPesadosPorAlmacen(
            @PathVariable Long almacenId,
            @RequestParam(defaultValue = "10.0") Double pesoMinimo) {
        return ResponseEntity.ok(residuoService.buscarPesadosPorAlmacen(almacenId, pesoMinimo));
    }

    /**
     * Consulta JPQL 5: Filtrar residuos gestionados por empresa y estado.
     * GET /api/residuos/jpql/empresa/{empresaId}/estado/{estado}
     */
    @GetMapping("/jpql/empresa/{empresaId}/estado/{estado}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OPERADOR', 'USER')")
    public ResponseEntity<List<ResiduoResponseDTO>> buscarPorEmpresaYEstado(
            @PathVariable Long empresaId,
            @PathVariable EstadoResiduo estado) {
        return ResponseEntity.ok(residuoService.buscarPorEmpresaGestoraYEstado(empresaId, estado));
    }
}
