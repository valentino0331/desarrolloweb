package com.utp.ecotechtrack.dto;

import com.utp.ecotechtrack.model.EstadoResiduo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para actualizar el estado del residuo en la cadena de trazabilidad.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CambioEstadoDTO {

    @NotNull(message = "El nuevo estado es obligatorio")
    private EstadoResiduo nuevoEstado;

    private String ubicacionActualizada;

    private String empresaGestora;

    private String observaciones;
}
