package com.utp.ecotechtrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la gestión de Almacenes y puntos de custodia de RAEE.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlmacenDTO {

    private Long id;

    @NotBlank(message = "El código de almacén es obligatorio (ej. ALM-TI-01)")
    @Size(max = 30, message = "El código no puede superar los 30 caracteres")
    private String codigo;

    @NotBlank(message = "El nombre del almacén es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotBlank(message = "La sede es obligatoria (ej. Sede Central, Pabellón A)")
    @Size(max = 80, message = "La sede no puede superar los 80 caracteres")
    private String sede;

    private String direccion;

    @Positive(message = "La capacidad debe ser un número positivo")
    private Double capacidadKg;

    private String responsable;
    private Integer totalResiduosAlmacenados;
}
