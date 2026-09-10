package com.utp.ecotechtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utp.ecotechtrack.dto.AlmacenDTO;
import com.utp.ecotechtrack.service.AlmacenService;
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

@WebMvcTest(AlmacenController.class)
public class AlmacenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AlmacenService almacenService;

    @Test
    @DisplayName("GET /api/almacenes - Debe retornar listado de almacenes con HTTP 200")
    void testListarAlmacenes() throws Exception {
        AlmacenDTO dto = AlmacenDTO.builder()
                .id(1L)
                .codigo("ALM-TI-01")
                .nombre("Almacén General de TI")
                .sede("Sede Central")
                .capacidadKg(1500.0)
                .build();

        when(almacenService.listarTodos()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/almacenes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codigo", is("ALM-TI-01")))
                .andExpect(jsonPath("$[0].nombre", is("Almacén General de TI")));
    }

    @Test
    @DisplayName("POST /api/almacenes - Debe crear un almacén válido con HTTP 201")
    void testRegistrarAlmacenExitoso() throws Exception {
        AlmacenDTO dto = AlmacenDTO.builder()
                .codigo("ALM-TI-01")
                .nombre("Almacén General de TI")
                .sede("Sede Central")
                .capacidadKg(1500.0)
                .build();

        AlmacenDTO creado = AlmacenDTO.builder()
                .id(1L)
                .codigo("ALM-TI-01")
                .nombre("Almacén General de TI")
                .sede("Sede Central")
                .capacidadKg(1500.0)
                .build();

        when(almacenService.registrar(any(AlmacenDTO.class))).thenReturn(creado);

        mockMvc.perform(post("/api/almacenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigo", is("ALM-TI-01")));
    }
}
