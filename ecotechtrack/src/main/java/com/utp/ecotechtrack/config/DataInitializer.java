package com.utp.ecotechtrack.config;

import com.utp.ecotechtrack.model.*;
import com.utp.ecotechtrack.repository.AlmacenRepository;
import com.utp.ecotechtrack.repository.EmpresaGestoraRepository;
import com.utp.ecotechtrack.repository.HistorialMovimientoRepository;
import com.utp.ecotechtrack.repository.ResiduoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Semillero de datos de prueba para EcoTechTrack.
 * Carga automáticamente Empresas Gestoras, Almacenes, Residuos y sus registros de Historial.
 */
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(ResiduoRepository residuoRepository,
                                   AlmacenRepository almacenRepository,
                                   EmpresaGestoraRepository empresaGestoraRepository,
                                   HistorialMovimientoRepository historialMovimientoRepository) {
        return args -> {
            if (almacenRepository.count() == 0 && empresaGestoraRepository.count() == 0) {

                // 1. Semillero de Empresas Gestoras de RAEE autorizadas por el MINAM
                EmpresaGestora eg1 = EmpresaGestora.builder()
                        .ruc("20601234567")
                        .razonSocial("EcoGestores del Perú S.A.C.")
                        .registroAutorizacion("EO-RS-0045-2022-MINAM")
                        .email("contacto@ecogestoresperu.com")
                        .telefono("+51 1 450-9820")
                        .direccion("Av. Materiales 2450, Cercado de Lima")
                        .build();

                EmpresaGestora eg2 = EmpresaGestora.builder()
                        .ruc("20559876543")
                        .razonSocial("ReciclaTech Ambiental S.A.")
                        .registroAutorizacion("EO-RS-0112-2023-MINAM")
                        .email("operaciones@reciclatech.pe")
                        .telefono("+51 1 614-3300")
                        .direccion("Calle Los Plásticos 180, Callao")
                        .build();

                empresaGestoraRepository.saveAll(List.of(eg1, eg2));

                // 2. Semillero de Almacenes / Puntos de Acopio Temporal
                Almacen a1 = Almacen.builder()
                        .codigo("ALM-TI-01")
                        .nombre("Almacén General de TI")
                        .sede("Sede Central - Sótano 1")
                        .direccion("Av. Arequipa 265, Lima")
                        .capacidadKg(1500.0)
                        .responsable("Ing. Carlos Mendoza (Jefe de Soporte TI)")
                        .build();

                Almacen a2 = Almacen.builder()
                        .codigo("ALM-DEP-02")
                        .nombre("Depósito Temporal de Equipos B")
                        .sede("Campus Norte - Pabellón de Ingeniería")
                        .direccion("Av. Alfredo Mendiola 6377, Los Olivos")
                        .capacidadKg(2000.0)
                        .responsable("Lic. Rosa Salazar (Almacenera)")
                        .build();

                Almacen a3 = Almacen.builder()
                        .codigo("ALM-SEG-03")
                        .nombre("Sala Técnica de Respaldo y Energía")
                        .sede("Campus Central - Azotea Torre A")
                        .direccion("Av. Arequipa 265, Lima")
                        .capacidadKg(800.0)
                        .responsable("Téc. Marcos Paredes (Infraestructura)")
                        .build();

                almacenRepository.saveAll(List.of(a1, a2, a3));

                // 3. Semillero de Residuos Electrónicos con relaciones
                ResiduoElectronico r1 = ResiduoElectronico.builder()
                        .codigoIdentificacion("RAEE-2026-001")
                        .descripcion("Servidor Torre HP ProLiant ML350 Gen9")
                        .categoria(CategoriaRAEE.EQUIPOS_INFORMATICA_Y_TELECOMUNICACIONES)
                        .marca("HP")
                        .modelo("ProLiant ML350 Gen9")
                        .numeroSerie("CZ25410ABCD")
                        .pesoKg(32.4)
                        .estado(EstadoResiduo.DECLARADO_BAJA)
                        .almacen(a1)
                        .empresaGestora(null)
                        .observaciones("Baja técnica por fin de ciclo de soporte empresarial")
                        .fechaRegistro(LocalDateTime.now().minusDays(6))
                        .build();

                ResiduoElectronico r2 = ResiduoElectronico.builder()
                        .codigoIdentificacion("RAEE-2026-002")
                        .descripcion("Lote de Monitores LED 24 pulgadas (10 unidades)")
                        .categoria(CategoriaRAEE.APARATOS_ELECTRONICOS_DE_CONSUMO)
                        .marca("Samsung")
                        .modelo("SyncMaster F24T350")
                        .numeroSerie("LOTE-SAM-24-001")
                        .pesoKg(35.0)
                        .estado(EstadoResiduo.ALMACENADO_TEMPORAL)
                        .almacen(a2)
                        .empresaGestora(null)
                        .observaciones("Pantallas con fallas de retroiluminación y paneles dañados")
                        .fechaRegistro(LocalDateTime.now().minusDays(4))
                        .build();

                ResiduoElectronico r3 = ResiduoElectronico.builder()
                        .codigoIdentificacion("RAEE-2026-003")
                        .descripcion("Banco de Baterías de Respaldo UPS Trifásico 10kVA")
                        .categoria(CategoriaRAEE.BATERIAS_Y_ACUMULADORES)
                        .marca("APC by Schneider")
                        .modelo("Smart-UPS VT 10kVA")
                        .numeroSerie("APC-UPS-789012")
                        .pesoKg(84.0)
                        .estado(EstadoResiduo.SOLICITUD_RECOJO)
                        .almacen(a3)
                        .empresaGestora(eg1)
                        .observaciones("Químicos agotados, requiere recojo con protocolo de materiales peligrosos")
                        .fechaRegistro(LocalDateTime.now().minusDays(3))
                        .build();

                ResiduoElectronico r4 = ResiduoElectronico.builder()
                        .codigoIdentificacion("RAEE-2026-004")
                        .descripcion("Lote de Laptops Lenovo ThinkPad T470 (5 unidades)")
                        .categoria(CategoriaRAEE.EQUIPOS_INFORMATICA_Y_TELECOMUNICACIONES)
                        .marca("Lenovo")
                        .modelo("ThinkPad T470")
                        .numeroSerie("LOTE-LEN-T470-2026")
                        .pesoKg(8.2)
                        .estado(EstadoResiduo.EN_TRANSITO)
                        .almacen(a1)
                        .empresaGestora(eg1)
                        .observaciones("Discos duros desmagnetizados y destruidos según política de privacidad")
                        .fechaRegistro(LocalDateTime.now().minusDays(2))
                        .build();

                residuoRepository.saveAll(List.of(r1, r2, r3, r4));

                // 4. Semillero de Historial de Trazabilidad
                HistorialMovimiento h1 = HistorialMovimiento.builder()
                        .residuo(r1)
                        .estadoAnterior(null)
                        .estadoNuevo(EstadoResiduo.DECLARADO_BAJA)
                        .fechaMovimiento(LocalDateTime.now().minusDays(6))
                        .responsable("Carlos Mendoza (Jefe TI)")
                        .observaciones("Declaración formal de baja en sistema")
                        .build();

                HistorialMovimiento h2_1 = HistorialMovimiento.builder()
                        .residuo(r2)
                        .estadoAnterior(null)
                        .estadoNuevo(EstadoResiduo.DECLARADO_BAJA)
                        .fechaMovimiento(LocalDateTime.now().minusDays(4))
                        .responsable("Rosa Salazar (Almacén)")
                        .observaciones("Recepción inicial")
                        .build();

                HistorialMovimiento h2_2 = HistorialMovimiento.builder()
                        .residuo(r2)
                        .estadoAnterior(EstadoResiduo.DECLARADO_BAJA)
                        .estadoNuevo(EstadoResiduo.ALMACENADO_TEMPORAL)
                        .fechaMovimiento(LocalDateTime.now().minusDays(3))
                        .responsable("Rosa Salazar (Almacén)")
                        .observaciones("Custodia en Depósito Temporal B")
                        .build();

                HistorialMovimiento h3_1 = HistorialMovimiento.builder()
                        .residuo(r3)
                        .estadoAnterior(EstadoResiduo.ALMACENADO_TEMPORAL)
                        .estadoNuevo(EstadoResiduo.SOLICITUD_RECOJO)
                        .fechaMovimiento(LocalDateTime.now().minusDays(2))
                        .responsable("Marcos Paredes (Seguridad)")
                        .observaciones("Solicitud de recojo enviada a EcoGestores del Perú")
                        .build();

                HistorialMovimiento h4_1 = HistorialMovimiento.builder()
                        .residuo(r4)
                        .estadoAnterior(EstadoResiduo.SOLICITUD_RECOJO)
                        .estadoNuevo(EstadoResiduo.EN_TRANSITO)
                        .fechaMovimiento(LocalDateTime.now().minusDays(1))
                        .responsable("Operador EcoGestores")
                        .observaciones("Guía de remisión emitida, carga en vehículo de transporte autorizado")
                        .build();

                historialMovimientoRepository.saveAll(List.of(h1, h2_1, h2_2, h3_1, h4_1));
            }
        };
    }
}
