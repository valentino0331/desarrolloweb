package com.utp.ecotechtrack.repository;

import com.utp.ecotechtrack.model.EmpresaGestora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaGestoraRepository extends JpaRepository<EmpresaGestora, Long> {

    Optional<EmpresaGestora> findByRuc(String ruc);

    boolean existsByRuc(String ruc);

    /**
     * Consulta JPQL para filtrar empresas gestoras por coincidencia en razón social o RUC.
     */
    @Query("SELECT eg FROM EmpresaGestora eg WHERE LOWER(eg.razonSocial) LIKE LOWER(CONCAT('%', :termino, '%')) OR eg.ruc LIKE CONCAT('%', :termino, '%')")
    List<EmpresaGestora> buscarPorTerminoJPQL(@Param("termino") String termino);
}
