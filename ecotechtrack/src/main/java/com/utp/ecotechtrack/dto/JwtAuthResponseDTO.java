package com.utp.ecotechtrack.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de respuesta para operaciones de autenticación exitosas.
 * Proporciona el token Bearer y la metadata del usuario para la sesión cliente (Angular/Postman).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtAuthResponseDTO {

    private String token;

    @Builder.Default
    private String tipoToken = "Bearer";

    private Long id;
    private String username;
    private String nombreCompleto;
    private String email;
    private List<String> roles;
}
