package com.utp.ecotechtrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una Empresa Operadora de Residuos Sólidos (EO-RS / EPS-RS)
 * autorizada por el Ministerio del Ambiente (MINAM) para el manejo de RAEE.
 */
@Entity
@Table(name = "empresas_gestoras")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "residuos")
@EqualsAndHashCode(exclude = "residuos")
public class EmpresaGestora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 11)
    private String ruc;

    @Column(name = "razon_social", nullable = false, length = 120)
    private String razonSocial;

    @Column(name = "registro_autorizacion", nullable = false, length = 80)
    private String registroAutorizacion;

    @Column(length = 100)
    private String email;

    @Column(length = 25)
    private String telefono;

    @Column(length = 150)
    private String direccion;

    @Builder.Default
    @OneToMany(mappedBy = "empresaGestora", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<ResiduoElectronico> residuos = new ArrayList<>();
}
