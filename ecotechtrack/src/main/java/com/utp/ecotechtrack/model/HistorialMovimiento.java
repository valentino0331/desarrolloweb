package com.utp.ecotechtrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad de auditoría y trazabilidad que registra cada cambio de estado,
 * movimiento físico o asignación de un residuo electrónico.
 */
@Entity
@Table(name = "historial_movimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HistorialMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residuo_id", nullable = false)
    @JsonIgnore
    private ResiduoElectronico residuo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_anterior", length = 40)
    private EstadoResiduo estadoAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_nuevo", nullable = false, length = 40)
    private EstadoResiduo estadoNuevo;

    @Column(name = "fecha_movimiento", nullable = false)
    private LocalDateTime fechaMovimiento;

    @Column(length = 100)
    private String responsable;

    @Column(length = 300)
    private String observaciones;

    @PrePersist
    public void prePersist() {
        if (this.fechaMovimiento == null) {
            this.fechaMovimiento = LocalDateTime.now();
        }
    }
}
