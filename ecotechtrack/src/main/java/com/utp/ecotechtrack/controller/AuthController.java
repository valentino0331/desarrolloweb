package com.utp.ecotechtrack.controller;

import com.utp.ecotechtrack.dto.JwtAuthResponseDTO;
import com.utp.ecotechtrack.dto.LoginRequestDTO;
import com.utp.ecotechtrack.dto.RegisterRequestDTO;
import com.utp.ecotechtrack.dto.UsuarioResponseDTO;
import com.utp.ecotechtrack.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador REST para endpoints de autenticación y autorización (Spring Security + JWT).
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para iniciar sesión y obtener el token JWT Bearer.
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest) {
        JwtAuthResponseDTO response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para el registro público de nuevos usuarios.
     * POST /api/auth/register
     */
    @PostMapping("/register")
    public ResponseEntity<UsuarioResponseDTO> register(@Valid @RequestBody RegisterRequestDTO registerRequest) {
        UsuarioResponseDTO response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint protegido para consultar el perfil del usuario autenticado actualmente.
     * GET /api/auth/me
     */
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioResponseDTO> getAuthenticatedUser(Authentication authentication) {
        UsuarioResponseDTO response = authService.getCurrentUser(authentication.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint utilitario para verificar si el token enviado en la cabecera es válido.
     * GET /api/auth/verificar
     */
    @GetMapping("/verificar")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> verificarToken(Authentication authentication) {
        return ResponseEntity.ok(Map.of(
                "valido", true,
                "username", authentication.getName(),
                "roles", authentication.getAuthorities()
        ));
    }
}
