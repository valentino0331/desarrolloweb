package com.utp.ecotechtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para devolver una estructura de error estandarizada y limpia al cliente REST.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponseDTO {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String mensaje;

    private String path;

    private List<String> detalles;
}
