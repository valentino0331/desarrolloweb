package com.utp.ecotechtrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa un almacén, sede o punto de acopio temporal de residuos electrónicos.
 */
@Entity
@Table(name = "almacenes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "residuos")
@EqualsAndHashCode(exclude = "residuos")
public class Almacen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 80)
    private String sede;

    @Column(length = 150)
    private String direccion;

    @Column(name = "capacidad_kg")
    private Double capacidadKg;

    @Column(length = 100)
    private String responsable;

    @Builder.Default
    @OneToMany(mappedBy = "almacen", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<ResiduoElectronico> residuos = new ArrayList<>();
}
