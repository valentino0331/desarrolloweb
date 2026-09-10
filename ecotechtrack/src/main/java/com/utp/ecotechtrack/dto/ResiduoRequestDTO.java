package com.utp.ecotechtrack.dto;

import com.utp.ecotechtrack.model.CategoriaRAEE;
import com.utp.ecotechtrack.model.EstadoResiduo;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Objeto de transferencia de datos (DTO) para la creación y actualización de un residuo electrónico.
 * Admite asignación relacional mediante almacenId y empresaGestoraId, o texto descriptivo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResiduoRequestDTO {

    @NotBlank(message = "El código de identificación es obligatorio (ej. RAEE-2026-001)")
    @Size(min = 4, max = 50, message = "El código debe tener entre 4 y 50 caracteres")
    private String codigoIdentificacion;

    @NotBlank(message = "La descripción del equipo/residuo es obligatoria")
    @Size(max = 120, message = "La descripción no puede superar los 120 caracteres")
    private String descripcion;

    @NotNull(message = "La categoría RAEE es obligatoria")
    private CategoriaRAEE categoria;

    @NotBlank(message = "La marca del equipo es obligatoria")
    private String marca;

    @NotBlank(message = "El modelo del equipo es obligatorio")
    private String modelo;

    private String numeroSerie;

    @NotNull(message = "El peso en kilogramos es obligatorio")
    @Positive(message = "El peso debe ser un valor positivo mayor a 0")
    @DecimalMin(value = "0.01", message = "El peso mínimo debe ser de 0.01 kg")
    private Double pesoKg;

    private EstadoResiduo estado;

    /**
     * ID de la entidad Almacen relacionada (opcional si se envía ubicacionAlmacen).
     */
    private Long almacenId;

    /**
     * Ubicación textual (mantenida para retrocompatibilidad o descripción puntual).
     */
    private String ubicacionAlmacen;

    /**
     * ID de la entidad EmpresaGestora relacionada.
     */
    private Long empresaGestoraId;

    /**
     * Nombre textual de la empresa gestora (para retrocompatibilidad).
     */
    private String empresaGestora;

    private String observaciones;
}
