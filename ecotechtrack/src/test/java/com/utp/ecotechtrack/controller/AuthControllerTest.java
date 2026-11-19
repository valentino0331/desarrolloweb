package com.utp.ecotechtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utp.ecotechtrack.dto.JwtAuthResponseDTO;
import com.utp.ecotechtrack.dto.LoginRequestDTO;
import com.utp.ecotechtrack.dto.RegisterRequestDTO;
import com.utp.ecotechtrack.dto.UsuarioResponseDTO;
import com.utp.ecotechtrack.model.Rol;
import com.utp.ecotechtrack.security.CustomUserDetailsService;
import com.utp.ecotechtrack.security.JwtAccessDeniedHandler;
import com.utp.ecotechtrack.security.JwtAuthenticationEntryPoint;
import com.utp.ecotechtrack.security.JwtTokenProvider;
import com.utp.ecotechtrack.service.AuthService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Test
    @DisplayName("POST /api/auth/login - Debe autenticar y retornar token JWT con HTTP 200")
    void testLoginExitoso() throws Exception {
        LoginRequestDTO request = LoginRequestDTO.builder()
                .username("admin")
                .password("Admin123!")
                .build();

        JwtAuthResponseDTO response = JwtAuthResponseDTO.builder()
                .token("mock-jwt-token-xyz-123456")
                .tipoToken("Bearer")
                .id(1L)
                .username("admin")
                .nombreCompleto("Administrador del Sistema")
                .email("admin@ecotechtrack.pe")
                .roles(List.of("ROLE_ADMIN", "ROLE_OPERADOR", "ROLE_USER"))
                .build();

        when(authService.login(any(LoginRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("mock-jwt-token-xyz-123456")))
                .andExpect(jsonPath("$.tipoToken", is("Bearer")))
                .andExpect(jsonPath("$.username", is("admin")))
                .andExpect(jsonPath("$.roles[0]", is("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("POST /api/auth/register - Debe registrar nuevo usuario con HTTP 201")
    void testRegisterExitoso() throws Exception {
        RegisterRequestDTO request = RegisterRequestDTO.builder()
                .username("nuevo_usuario")
                .password("ClaveSegura2026!")
                .nombreCompleto("Nuevo Usuario Auditor")
                .email("nuevo@ecotechtrack.pe")
                .roles(Set.of(Rol.ROLE_USER))
                .build();

        UsuarioResponseDTO response = UsuarioResponseDTO.builder()
                .id(10L)
                .username("nuevo_usuario")
                .nombreCompleto("Nuevo Usuario Auditor")
                .email("nuevo@ecotechtrack.pe")
                .activo(true)
                .roles(Set.of(Rol.ROLE_USER))
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(authService.register(any(RegisterRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.username", is("nuevo_usuario")))
                .andExpect(jsonPath("$.email", is("nuevo@ecotechtrack.pe")));
    }
}
