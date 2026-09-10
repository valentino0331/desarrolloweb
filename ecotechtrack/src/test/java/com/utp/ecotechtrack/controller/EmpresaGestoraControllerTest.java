package com.utp.ecotechtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utp.ecotechtrack.dto.EmpresaGestoraDTO;
import com.utp.ecotechtrack.service.EmpresaGestoraService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmpresaGestoraController.class)
public class EmpresaGestoraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmpresaGestoraService empresaGestoraService;

    @Test
    @DisplayName("GET /api/empresas-gestoras - Debe retornar listado de empresas con HTTP 200")
    void testListarEmpresas() throws Exception {
        EmpresaGestoraDTO dto = EmpresaGestoraDTO.builder()
                .id(1L)
                .ruc("20601234567")
                .razonSocial("EcoGestores del Perú S.A.C.")
                .registroAutorizacion("EO-RS-0045-2022-MINAM")
                .build();

        when(empresaGestoraService.listarTodas()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/empresas-gestoras")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].ruc", is("20601234567")))
                .andExpect(jsonPath("$[0].razonSocial", is("EcoGestores del Perú S.A.C.")));
    }

    @Test
    @DisplayName("POST /api/empresas-gestoras - Debe registrar empresa válida con HTTP 201")
    void testRegistrarEmpresaExitosa() throws Exception {
        EmpresaGestoraDTO dto = EmpresaGestoraDTO.builder()
                .ruc("20601234567")
                .razonSocial("EcoGestores del Perú S.A.C.")
                .registroAutorizacion("EO-RS-0045-2022-MINAM")
                .email("info@ecogestores.pe")
                .build();

        EmpresaGestoraDTO creada = EmpresaGestoraDTO.builder()
                .id(1L)
                .ruc("20601234567")
                .razonSocial("EcoGestores del Perú S.A.C.")
                .registroAutorizacion("EO-RS-0045-2022-MINAM")
                .email("info@ecogestores.pe")
                .build();

        when(empresaGestoraService.registrar(any(EmpresaGestoraDTO.class))).thenReturn(creada);

        mockMvc.perform(post("/api/empresas-gestoras")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.ruc", is("20601234567")));
    }
}
