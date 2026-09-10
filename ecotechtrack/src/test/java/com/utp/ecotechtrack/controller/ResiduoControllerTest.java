package com.utp.ecotechtrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.utp.ecotechtrack.dto.CambioEstadoDTO;
import com.utp.ecotechtrack.dto.ResiduoRequestDTO;
import com.utp.ecotechtrack.dto.ResiduoResponseDTO;
import com.utp.ecotechtrack.exception.ResourceNotFoundException;
import com.utp.ecotechtrack.model.CategoriaRAEE;
import com.utp.ecotechtrack.model.EstadoResiduo;
import com.utp.ecotechtrack.service.ResiduoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias de controlador web (TDD) para ResiduoController.
 * Utiliza MockMvc y @WebMvcTest conforme a las enseñanzas de la Sesión 3 (TDD).
 */
@WebMvcTest(ResiduoController.class)
public class ResiduoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResiduoService residuoService;

    private ResiduoResponseDTO sampleResponseDTO;
    private ResiduoRequestDTO sampleRequestDTO;

    @BeforeEach
    void setUp() {
        sampleRequestDTO = ResiduoRequestDTO.builder()
                .codigoIdentificacion("RAEE-2026-001")
                .descripcion("Servidor Dell PowerEdge R740 fuera de uso")
                .categoria(CategoriaRAEE.EQUIPOS_INFORMATICA_Y_TELECOMUNICACIONES)
                .marca("Dell")
                .modelo("PowerEdge R740")
                .numeroSerie("SN-DELL-98765")
                .pesoKg(28.5)
                .estado(EstadoResiduo.DECLARADO_BAJA)
                .ubicacionAlmacen("Almacén Central TI - Estante B3")
                .empresaGestora("ReciclaTech Perú S.A.C.")
                .observaciones("Baja por obsolescencia tecnológica")
                .build();

        sampleResponseDTO = ResiduoResponseDTO.builder()
                .id(1L)
                .codigoIdentificacion("RAEE-2026-001")
                .descripcion("Servidor Dell PowerEdge R740 fuera de uso")
                .categoria(CategoriaRAEE.EQUIPOS_INFORMATICA_Y_TELECOMUNICACIONES)
                .marca("Dell")
                .modelo("PowerEdge R740")
                .numeroSerie("SN-DELL-98765")
                .pesoKg(28.5)
                .estado(EstadoResiduo.DECLARADO_BAJA)
                .ubicacionAlmacen("Almacén Central TI - Estante B3")
                .empresaGestora("ReciclaTech Perú S.A.C.")
                .observaciones("Baja por obsolescencia tecnológica")
                .fechaRegistro(LocalDateTime.now())
                .fechaActualizacion(null)
                .build();
    }

    @Test
    @DisplayName("GET /api/residuos - Debe retornar listado de residuos con HTTP 200")
    void testListarResiduosExitoso() throws Exception {
        when(residuoService.listarTodos()).thenReturn(List.of(sampleResponseDTO));

        mockMvc.perform(get("/api/residuos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].codigoIdentificacion", is("RAEE-2026-001")))
                .andExpect(jsonPath("$[0].marca", is("Dell")))
                .andExpect(jsonPath("$[0].pesoKg", is(28.5)));

        verify(residuoService, times(1)).listarTodos();
    }

    @Test
    @DisplayName("GET /api/residuos/1 - Debe retornar un residuo existente con HTTP 200")
    void testObtenerPorIdExitoso() throws Exception {
        when(residuoService.obtenerPorId(1L)).thenReturn(sampleResponseDTO);

        mockMvc.perform(get("/api/residuos/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigoIdentificacion", is("RAEE-2026-001")))
                .andExpect(jsonPath("$.descripcion", containsString("Dell PowerEdge")));

        verify(residuoService, times(1)).obtenerPorId(1L);
    }

    @Test
    @DisplayName("GET /api/residuos/999 - Debe retornar HTTP 404 cuando el residuo no existe")
    void testObtenerPorIdNoEncontrado() throws Exception {
        when(residuoService.obtenerPorId(999L))
                .thenThrow(new ResourceNotFoundException("No se encontró el residuo electrónico con ID: 999"));

        mockMvc.perform(get("/api/residuos/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.mensaje", containsString("ID: 999")));

        verify(residuoService, times(1)).obtenerPorId(999L);
    }

    @Test
    @DisplayName("POST /api/residuos - Debe crear un residuo válido con HTTP 201 Created")
    void testRegistrarResiduoExitoso() throws Exception {
        when(residuoService.registrar(any(ResiduoRequestDTO.class))).thenReturn(sampleResponseDTO);

        mockMvc.perform(post("/api/residuos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.codigoIdentificacion", is("RAEE-2026-001")))
                .andExpect(jsonPath("$.estado", is("DECLARADO_BAJA")));

        verify(residuoService, times(1)).registrar(any(ResiduoRequestDTO.class));
    }

    @Test
    @DisplayName("POST /api/residuos - Debe retornar HTTP 400 Bad Request cuando faltan campos obligatorios")
    void testRegistrarResiduoConValidacionFallida() throws Exception {
        ResiduoRequestDTO invalido = ResiduoRequestDTO.builder()
                .codigoIdentificacion("") // Inválido: NotBlank
                .descripcion("")          // Inválido: NotBlank
                .pesoKg(-5.0)             // Inválido: Positive
                .build();

        mockMvc.perform(post("/api/residuos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.error", is("Error de Validación de Datos")))
                .andExpect(jsonPath("$.detalles", hasSize(greaterThan(0))));

        verify(residuoService, never()).registrar(any());
    }

    @Test
    @DisplayName("PATCH /api/residuos/1/estado - Debe actualizar la trazabilidad con HTTP 200 OK")
    void testCambiarEstadoExitoso() throws Exception {
        CambioEstadoDTO cambioDTO = CambioEstadoDTO.builder()
                .nuevoEstado(EstadoResiduo.EN_TRANSITO)
                .ubicacionActualizada("Camión cisterna de recojo - Placa ABC-123")
                .empresaGestora("ReciclaTech Perú S.A.C.")
                .observaciones("En ruta hacia planta de valorización")
                .build();

        ResiduoResponseDTO modificado = ResiduoResponseDTO.builder()
                .id(1L)
                .codigoIdentificacion("RAEE-2026-001")
                .estado(EstadoResiduo.EN_TRANSITO)
                .ubicacionAlmacen("Camión cisterna de recojo - Placa ABC-123")
                .empresaGestora("ReciclaTech Perú S.A.C.")
                .build();

        when(residuoService.cambiarEstado(eq(1L), any(CambioEstadoDTO.class))).thenReturn(modificado);

        mockMvc.perform(patch("/api/residuos/1/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cambioDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.estado", is("EN_TRANSITO")));

        verify(residuoService, times(1)).cambiarEstado(eq(1L), any(CambioEstadoDTO.class));
    }

    @Test
    @DisplayName("DELETE /api/residuos/1 - Debe eliminar residuo con HTTP 204 No Content")
    void testEliminarResiduoExitoso() throws Exception {
        doNothing().when(residuoService).eliminar(1L);

        mockMvc.perform(delete("/api/residuos/1"))
                .andExpect(status().isNoContent());

        verify(residuoService, times(1)).eliminar(1L);
    }
}
