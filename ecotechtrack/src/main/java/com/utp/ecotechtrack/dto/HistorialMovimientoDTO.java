package com.utp.ecotechtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utp.ecotechtrack.model.EstadoResiduo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para la consulta de eventos de auditoría y trazabilidad de un residuo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialMovimientoDTO {

    private Long id;
    private Long residuoId;
    private String codigoResiduo;
    private EstadoResiduo estadoAnterior;
    private EstadoResiduo estadoNuevo;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaMovimiento;

    private String responsable;
    private String observaciones;
}
