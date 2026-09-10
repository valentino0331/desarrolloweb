package com.utp.ecotechtrack.controller;

import com.utp.ecotechtrack.dto.CambioEstadoDTO;
import com.utp.ecotechtrack.dto.HistorialMovimientoDTO;
import com.utp.ecotechtrack.dto.ResiduoRequestDTO;
import com.utp.ecotechtrack.dto.ResiduoResponseDTO;
import com.utp.ecotechtrack.model.CategoriaRAEE;
import com.utp.ecotechtrack.model.EstadoResiduo;
import com.utp.ecotechtrack.service.ResiduoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión y trazabilidad de Residuos Electrónicos (RAEE).
 * Cumple con los estándares RESTful, códigos de estado HTTP y Dependency Injection por constructor.
 */
@RestController
@RequestMapping("/api/residuos")
@CrossOrigin(origins = "*") // Permite el consumo futuro desde Angular
public class ResiduoController {

    private final ResiduoService residuoService;

    // Inyección de dependencias por constructor (patrón recomendado UTP S02)
    public ResiduoController(ResiduoService residuoService) {
        this.residuoService = residuoService;
    }

    /**
     * Endpoint para listar todos los residuos, con filtros opcionales por estado o categoría.
     * GET http://localhost:8080/api/residuos
     * GET http://localhost:8080/api/residuos?estado=DECLARADO_BAJA
     * GET http://localhost:8080/api/residuos?categoria=EQUIPOS_INFORMATICA_Y_TELECOMUNICACIONES
     */
    @GetMapping
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
     * Endpoint para obtener un residuo específico por su ID primario.
     * GET http://localhost:8080/api/residuos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ResiduoResponseDTO> obtenerPorId(@PathVariable Long id) {
        ResiduoResponseDTO residuo = residuoService.obtenerPorId(id);
        return ResponseEntity.ok(residuo);
    }

    /**
     * Endpoint para consultar un residuo por su código único de identificación RAEE.
     * GET http://localhost:8080/api/residuos/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ResiduoResponseDTO> obtenerPorCodigo(@PathVariable String codigo) {
        ResiduoResponseDTO residuo = residuoService.obtenerPorCodigo(codigo);
        return ResponseEntity.ok(residuo);
    }

    /**
     * Endpoint para consultar el historial completo de trazabilidad y movimientos de un residuo.
     * GET http://localhost:8080/api/residuos/{id}/historial
     */
    @GetMapping("/{id}/historial")
    public ResponseEntity<List<HistorialMovimientoDTO>> obtenerHistorial(@PathVariable Long id) {
        return ResponseEntity.ok(residuoService.obtenerHistorial(id));
    }

    /**
     * Endpoint para dar de baja y registrar un nuevo residuo electrónico en el sistema.
     * POST http://localhost:8080/api/residuos
     */
    @PostMapping
    public ResponseEntity<ResiduoResponseDTO> registrarResiduo(@Valid @RequestBody ResiduoRequestDTO requestDTO) {
        ResiduoResponseDTO nuevoResiduo = residuoService.registrar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoResiduo);
    }

    /**
     * Endpoint para actualizar los datos completos de un residuo electrónico existente.
     * PUT http://localhost:8080/api/residuos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ResiduoResponseDTO> actualizarResiduo(
            @PathVariable Long id,
            @Valid @RequestBody ResiduoRequestDTO requestDTO) {
        ResiduoResponseDTO actualizado = residuoService.actualizar(id, requestDTO);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Endpoint para avanzar o cambiar el estado en la cadena de trazabilidad del residuo.
     * PATCH http://localhost:8080/api/residuos/{id}/estado
     */
    @PatchMapping("/{id}/estado")
    public ResponseEntity<ResiduoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoDTO cambioEstadoDTO) {
        ResiduoResponseDTO actualizado = residuoService.cambiarEstado(id, cambioEstadoDTO);
        return ResponseEntity.ok(actualizado);
    }

    /**
     * Endpoint para eliminar físicamente un residuo electrónico.
     * DELETE http://localhost:8080/api/residuos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResiduo(@PathVariable Long id) {
        residuoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
