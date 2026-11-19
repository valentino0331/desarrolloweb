package com.utp.ecotechtrack.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.utp.ecotechtrack.model.CategoriaRAEE;
import com.utp.ecotechtrack.model.EstadoResiduo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Objeto de transferencia de datos (DTO) para la respuesta de la API REST de residuos electrónicos.
 * Expone tanto los atributos propios como la información de las entidades relacionadas (Almacen y EmpresaGestora).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResiduoResponseDTO {

    private Long id;
    private String codigoIdentificacion;
    private String descripcion;
    private CategoriaRAEE categoria;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private Double pesoKg;
    private EstadoResiduo estado;

    // Relación con Almacén
    private Long almacenId;
    private String ubicacionAlmacen;
    private AlmacenDTO almacen;

    // Relación con Empresa Gestora
    private Long empresaGestoraId;
    private String empresaGestora;
    private EmpresaGestoraDTO empresaGestoraDetalle;

    private String observaciones;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaRegistro;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime fechaActualizacion;
}
