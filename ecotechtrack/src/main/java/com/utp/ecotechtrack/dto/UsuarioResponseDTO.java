package com.utp.ecotechtrack.dto;

import com.utp.ecotechtrack.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO para transferir información de un usuario sin exponer el hash de su contraseña.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private String nombreCompleto;
    private String email;
    private Boolean activo;
    private Set<Rol> roles;
    private LocalDateTime fechaCreacion;
}
