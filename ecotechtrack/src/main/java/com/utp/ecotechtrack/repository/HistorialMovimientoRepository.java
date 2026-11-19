package com.utp.ecotechtrack.repository;

import com.utp.ecotechtrack.model.HistorialMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistorialMovimientoRepository extends JpaRepository<HistorialMovimiento, Long> {

    List<HistorialMovimiento> findByResiduoIdOrderByFechaMovimientoDesc(Long residuoId);
}
