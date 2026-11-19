package com.utp.ecotechtrack.service;

import com.utp.ecotechtrack.dto.JwtAuthResponseDTO;
import com.utp.ecotechtrack.dto.LoginRequestDTO;
import com.utp.ecotechtrack.dto.RegisterRequestDTO;
import com.utp.ecotechtrack.dto.UsuarioResponseDTO;

/**
 * Interfaz de negocio para operaciones de seguridad, autenticación y registro de usuarios.
 */
public interface AuthService {

    JwtAuthResponseDTO login(LoginRequestDTO loginRequest);

    UsuarioResponseDTO register(RegisterRequestDTO registerRequest);

    UsuarioResponseDTO getCurrentUser(String username);
}
