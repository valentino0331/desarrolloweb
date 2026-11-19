package com.utp.ecotechtrack.repository;

import com.utp.ecotechtrack.dto.EstadisticaEstadoDTO;
import com.utp.ecotechtrack.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Pruebas unitarias con @DataJpaTest para validar las consultas personalizadas JPQL (Requisito APF2).
 */
@DataJpaTest
public class ResiduoRepositoryJPQLTest {

    @Autowired
    private ResiduoRepository residuoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Almacen almacen;
    private EmpresaGestora empresa;

    @BeforeEach
    void setUp() {
        almacen = Almacen.builder()
                .codigo("ALM-TEST-01")
                .nombre("Almacén Pruebas")
                .sede("Sede Central")
                .capacidadKg(1000.0)
                .build();
        entityManager.persist(almacen);

        empresa = EmpresaGestora.builder()
                .ruc("20111222333")
                .razonSocial("EcoGestión Test S.A.C.")
                .registroAutorizacion("EO-RS-9999-2026")
                .build();
        entityManager.persist(empresa);

        ResiduoElectronico r1 = ResiduoElectronico.builder()
                .codigoIdentificacion("RAEE-JPQL-01")
                .descripcion("Servidor de Archivos")
                .categoria(CategoriaRAEE.EQUIPOS_INFORMATICA_Y_TELECOMUNICACIONES)
                .marca("IBM")
                .modelo("System x3650")
                .pesoKg(40.0)
                .estado(EstadoResiduo.DECLARADO_BAJA)
                .almacen(almacen)
                .fechaRegistro(LocalDateTime.now().minusDays(5))
                .build();

        ResiduoElectronico r2 = ResiduoElectronico.builder()
                .codigoIdentificacion("RAEE-JPQL-02")
                .descripcion("Pantalla Monitor LED")
                .categoria(CategoriaRAEE.APARATOS_ELECTRONICOS_DE_CONSUMO)
                .marca("Dell")
                .modelo("P2419H")
                .pesoKg(5.5)
                .estado(EstadoResiduo.EN_TRANSITO)
                .almacen(almacen)
                .empresaGestora(empresa)
                .fechaRegistro(LocalDateTime.now().minusDays(1))
                .build();

        entityManager.persist(r1);
        entityManager.persist(r2);
        entityManager.flush();
    }

    @Test
    @DisplayName("JPQL: Búsqueda por rango de fechas")
    void testFindByRangoFechasJPQL() {
        LocalDateTime inicio = LocalDateTime.now().minusDays(10);
        LocalDateTime fin = LocalDateTime.now();

        List<ResiduoElectronico> resultado = residuoRepository.findByRangoFechasJPQL(inicio, fin);
        assertThat(resultado).hasSize(2);
    }

    @Test
    @DisplayName("JPQL: Búsqueda multicriterio avanzada")
    void testBusquedaAvanzadaJPQL() {
        List<ResiduoElectronico> resultado = residuoRepository.busquedaAvanzadaJPQL(
                EstadoResiduo.DECLARADO_BAJA,
                CategoriaRAEE.EQUIPOS_INFORMATICA_Y_TELECOMUNICACIONES,
                "IBM"
        );

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigoIdentificacion()).isEqualTo("RAEE-JPQL-01");
    }

    @Test
    @DisplayName("JPQL: Estadísticas agregadas por estado")
    void testObtenerEstadisticasPorEstadoJPQL() {
        List<EstadisticaEstadoDTO> stats = residuoRepository.obtenerEstadisticasPorEstadoJPQL();

        assertThat(stats).isNotEmpty();
        assertThat(stats.stream().anyMatch(s -> s.getEstado() == EstadoResiduo.DECLARADO_BAJA && s.getCantidad() == 1)).isTrue();
    }

    @Test
    @DisplayName("JPQL: Residuos pesados por almacén")
    void testFindResiduosPesadosPorAlmacenJPQL() {
        List<ResiduoElectronico> pesados = residuoRepository.findResiduosPesadosPorAlmacenJPQL(almacen.getId(), 20.0);

        assertThat(pesados).hasSize(1);
        assertThat(pesados.get(0).getPesoKg()).isGreaterThanOrEqualTo(20.0);
    }
}
