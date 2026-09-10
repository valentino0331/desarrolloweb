package com.utp.ecotechtrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad principal de persistencia que representa un residuo o equipo electrónico dado de baja.
 * Mantiene relaciones con Almacen, EmpresaGestora y su HistorialMovimiento.
 */
@Entity
@Table(name = "residuos_electronicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"almacen", "empresaGestora", "historialMovimientos"})
@EqualsAndHashCode(exclude = {"almacen", "empresaGestora", "historialMovimientos"})
public class ResiduoElectronico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo_identificacion", nullable = false, unique = true, length = 50)
    private String codigoIdentificacion;

    @Column(nullable = false, length = 120)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private CategoriaRAEE categoria;

    @Column(nullable = false, length = 60)
    private String marca;

    @Column(nullable = false, length = 60)
    private String modelo;

    @Column(name = "numero_serie", length = 80)
    private String numeroSerie;

    @Column(name = "peso_kg", nullable = false)
    private Double pesoKg;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EstadoResiduo estado;

    /**
     * Relación con el Almacén o punto de custodia temporal del residuo.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "almacen_id")
    private Almacen almacen;

    /**
     * Relación con la Empresa Operadora / Gestora autorizada de RAEE.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empresa_gestora_id")
    private EmpresaGestora empresaGestora;

    @Column(name = "observaciones", length = 300)
    private String observaciones;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    /**
     * Historial de eventos y trazabilidad del residuo a lo largo de su ciclo de vida.
     */
    @Builder.Default
    @OneToMany(mappedBy = "residuo", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("fechaMovimiento DESC")
    @JsonIgnore
    private List<HistorialMovimiento> historialMovimientos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.fechaRegistro = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = EstadoResiduo.DECLARADO_BAJA;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void agregarMovimiento(HistorialMovimiento movimiento) {
        if (this.historialMovimientos == null) {
            this.historialMovimientos = new ArrayList<>();
        }
        movimiento.setResiduo(this);
        this.historialMovimientos.add(movimiento);
    }
}
