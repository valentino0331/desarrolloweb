package com.utp.ecotechtrack.repository;

import com.utp.ecotechtrack.dto.EstadisticaEstadoDTO;
import com.utp.ecotechtrack.model.CategoriaRAEE;
import com.utp.ecotechtrack.model.EstadoResiduo;
import com.utp.ecotechtrack.model.ResiduoElectronico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad ResiduoElectronico.
 * Incorpora métodos CRUD estándar y consultas JPQL personalizadas avanzadas.
 */
@Repository
public interface ResiduoRepository extends JpaRepository<ResiduoElectronico, Long> {

    Optional<ResiduoElectronico> findByCodigoIdentificacion(String codigoIdentificacion);

    boolean existsByCodigoIdentificacion(String codigoIdentificacion);

    List<ResiduoElectronico> findByEstado(EstadoResiduo estado);

    List<ResiduoElectronico> findByCategoria(CategoriaRAEE categoria);

    List<ResiduoElectronico> findByAlmacenId(Long almacenId);

    List<ResiduoElectronico> findByEmpresaGestoraId(Long empresaGestoraId);

    // =========================================================================
    // CONSULTAS PERSONALIZADAS CON JPQL (Requisito APF2)
    // =========================================================================

    /**
     * Consulta JPQL 1: Filtrar residuos registrados dentro de un rango de fechas.
     */
    @Query("SELECT r FROM ResiduoElectronico r WHERE r.fechaRegistro BETWEEN :fechaInicio AND :fechaFin ORDER BY r.fechaRegistro DESC")
    List<ResiduoElectronico> findByRangoFechasJPQL(
            @Param("fechaInicio") LocalDateTime fechaInicio,
            @Param("fechaFin") LocalDateTime fechaFin
    );

    /**
     * Consulta JPQL 2: Búsqueda multicriterio con filtros opcionales de estado, categoría y texto libre.
     */
    @Query("SELECT r FROM ResiduoElectronico r WHERE " +
           "(:estado IS NULL OR r.estado = :estado) AND " +
           "(:categoria IS NULL OR r.categoria = :categoria) AND " +
           "(:termino IS NULL OR LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(r.marca) LIKE LOWER(CONCAT('%', :termino, '%')) " +
           "OR LOWER(r.modelo) LIKE LOWER(CONCAT('%', :termino, '%')))")
    List<ResiduoElectronico> busquedaAvanzadaJPQL(
            @Param("estado") EstadoResiduo estado,
            @Param("categoria") CategoriaRAEE categoria,
            @Param("termino") String termino
    );

    /**
     * Consulta JPQL 3: Agregación estadística proyectada a DTO (conteo y suma de peso por cada estado).
     */
    @Query("SELECT new com.utp.ecotechtrack.dto.EstadisticaEstadoDTO(r.estado, COUNT(r), COALESCE(SUM(r.pesoKg), 0.0)) " +
           "FROM ResiduoElectronico r GROUP BY r.estado")
    List<EstadisticaEstadoDTO> obtenerEstadisticasPorEstadoJPQL();

    /**
     * Consulta JPQL 4: Filtrar residuos con peso superior o igual a un umbral en un almacén específico.
     */
    @Query("SELECT r FROM ResiduoElectronico r WHERE r.almacen.id = :almacenId AND r.pesoKg >= :pesoMinimo ORDER BY r.pesoKg DESC")
    List<ResiduoElectronico> findResiduosPesadosPorAlmacenJPQL(
            @Param("almacenId") Long almacenId,
            @Param("pesoMinimo") Double pesoMinimo
    );

    /**
     * Consulta JPQL 5: Filtrar residuos asignados a una empresa gestora específica en un estado determinado.
     */
    @Query("SELECT r FROM ResiduoElectronico r WHERE r.empresaGestora.id = :empresaId AND r.estado = :estado")
    List<ResiduoElectronico> findByEmpresaGestoraAndEstadoJPQL(
            @Param("empresaId") Long empresaId,
            @Param("estado") EstadoResiduo estado
    );
}
