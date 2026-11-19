package com.utp.ecotechtrack.service;

import com.utp.ecotechtrack.dto.CambioEstadoDTO;
import com.utp.ecotechtrack.dto.HistorialMovimientoDTO;
import com.utp.ecotechtrack.dto.ResiduoRequestDTO;
import com.utp.ecotechtrack.dto.ResiduoResponseDTO;
import com.utp.ecotechtrack.model.CategoriaRAEE;
import com.utp.ecotechtrack.model.EstadoResiduo;

import java.util.List;

/**
 * Contrato del servicio de negocio para la gestión y trazabilidad de RAEE.
 */
public interface ResiduoService {

    List<ResiduoResponseDTO> listarTodos();

    ResiduoResponseDTO obtenerPorId(Long id);

    ResiduoResponseDTO obtenerPorCodigo(String codigoIdentificacion);

    List<ResiduoResponseDTO> listarPorEstado(EstadoResiduo estado);

    List<ResiduoResponseDTO> listarPorCategoria(CategoriaRAEE categoria);

    ResiduoResponseDTO registrar(ResiduoRequestDTO requestDTO);

    ResiduoResponseDTO actualizar(Long id, ResiduoRequestDTO requestDTO);

    ResiduoResponseDTO cambiarEstado(Long id, CambioEstadoDTO cambioEstadoDTO);

    List<HistorialMovimientoDTO> obtenerHistorial(Long residuoId);

    void eliminar(Long id);

    // =========================================================================
    // MÉTODOS DE NEGOCIO BASADOS EN CONSULTAS JPQL (Requisito APF2)
    // =========================================================================

    List<ResiduoResponseDTO> buscarPorRangoFechas(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);

    List<ResiduoResponseDTO> busquedaAvanzada(EstadoResiduo estado, CategoriaRAEE categoria, String termino);

    List<com.utp.ecotechtrack.dto.EstadisticaEstadoDTO> obtenerEstadisticasPorEstado();

    List<ResiduoResponseDTO> buscarPesadosPorAlmacen(Long almacenId, Double pesoMinimo);

    List<ResiduoResponseDTO> buscarPorEmpresaGestoraYEstado(Long empresaId, EstadoResiduo estado);
}
