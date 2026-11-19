package com.utp.ecotechtrack.dto;

import com.utp.ecotechtrack.model.EstadoResiduo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para representar el resumen agregado calculado mediante consultas JPQL personalizadas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadisticaEstadoDTO {
    private EstadoResiduo estado;
    private Long cantidad;
    private Double pesoTotalKg;
}
