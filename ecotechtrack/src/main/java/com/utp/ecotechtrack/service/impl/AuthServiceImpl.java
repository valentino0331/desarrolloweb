package com.utp.ecotechtrack.service.impl;

import com.utp.ecotechtrack.dto.JwtAuthResponseDTO;
import com.utp.ecotechtrack.dto.LoginRequestDTO;
import com.utp.ecotechtrack.dto.RegisterRequestDTO;
import com.utp.ecotechtrack.dto.UsuarioResponseDTO;
import com.utp.ecotechtrack.exception.BadRequestException;
import com.utp.ecotechtrack.exception.ResourceNotFoundException;
import com.utp.ecotechtrack.model.Rol;
import com.utp.ecotechtrack.model.Usuario;
import com.utp.ecotechtrack.repository.UsuarioRepository;
import com.utp.ecotechtrack.security.CustomUserDetails;
import com.utp.ecotechtrack.security.JwtTokenProvider;
import com.utp.ecotechtrack.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación de la capa de servicio de autenticación y seguridad con transacciones declarativas.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public JwtAuthResponseDTO login(LoginRequestDTO loginRequest) {
        log.info("Intento de autenticación para el usuario: {}", loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateToken(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        log.info("Autenticación exitosa para: {} con roles: {}", userDetails.getUsername(), roles);

        return JwtAuthResponseDTO.builder()
                .token(jwt)
                .tipoToken("Bearer")
                .id(userDetails.getId())
                .username(userDetails.getUsername())
                .nombreCompleto(userDetails.getNombreCompleto())
                .email(userDetails.getEmail())
                .roles(roles)
                .build();
    }

    @Override
    @Transactional
    public UsuarioResponseDTO register(RegisterRequestDTO registerRequest) {
        log.info("Solicitud de registro de nuevo usuario: {}", registerRequest.getUsername());

        if (usuarioRepository.existsByUsername(registerRequest.getUsername())) {
            throw new BadRequestException("El nombre de usuario '" + registerRequest.getUsername() + "' ya se encuentra en uso.");
        }

        if (usuarioRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("El correo electrónico '" + registerRequest.getEmail() + "' ya está registrado.");
        }

        Set<Rol> roles = registerRequest.getRoles();
        if (roles == null || roles.isEmpty()) {
            roles = new HashSet<>();
            roles.add(Rol.ROLE_USER);
        }

        Usuario usuario = Usuario.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .nombreCompleto(registerRequest.getNombreCompleto())
                .email(registerRequest.getEmail())
                .activo(true)
                .roles(roles)
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        log.info("Usuario registrado exitosamente con ID: {}", guardado.getId());

        return mapToResponse(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponseDTO getCurrentUser(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con username: " + username));

        return mapToResponse(usuario);
    }

    private UsuarioResponseDTO mapToResponse(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .username(usuario.getUsername())
                .nombreCompleto(usuario.getNombreCompleto())
                .email(usuario.getEmail())
                .activo(usuario.getActivo())
                .roles(usuario.getRoles())
                .fechaCreacion(usuario.getFechaCreacion())
                .build();
    }
}
