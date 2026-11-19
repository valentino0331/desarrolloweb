package com.utp.ecotechtrack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la gestión de Empresas Gestoras de RAEE autorizadas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmpresaGestoraDTO {

    private Long id;

    @NotBlank(message = "El RUC es obligatorio")
    @Pattern(regexp = "^[0-9]{11}$", message = "El RUC debe constar de 11 dígitos numéricos")
    private String ruc;

    @NotBlank(message = "La razón social es obligatoria")
    @Size(max = 120, message = "La razón social no puede superar los 120 caracteres")
    private String razonSocial;

    @NotBlank(message = "El código de registro de autorización (MINAM/DIGESA) es obligatorio")
    @Size(max = 80, message = "El registro de autorización no puede superar 80 caracteres")
    private String registroAutorizacion;

    private String email;
    private String telefono;
    private String direccion;
    private Integer totalResiduosAsignados;
}
