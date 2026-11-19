package com.utp.ecotechtrack.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.utp.ecotechtrack.dto.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * Manejador que intercepta peticiones no autenticadas (HTTP 401 Unauthorized)
 * y retorna una respuesta JSON estructurada conforme a la API REST.
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponseDTO errorDTO = ErrorResponseDTO.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .error("Unauthorized")
                .mensaje("Acceso no autorizado: Debe proporcionar un token JWT válido en la cabecera 'Authorization: Bearer <token>'")
                .path(request.getRequestURI())
                .detalles(Collections.singletonList(authException.getMessage()))
                .build();

        response.getWriter().write(objectMapper.writeValueAsString(errorDTO));
    }
}
