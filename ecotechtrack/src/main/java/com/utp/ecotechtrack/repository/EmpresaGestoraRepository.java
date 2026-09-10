package com.utp.ecotechtrack.repository;

import com.utp.ecotechtrack.model.EmpresaGestora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmpresaGestoraRepository extends JpaRepository<EmpresaGestora, Long> {

    Optional<EmpresaGestora> findByRuc(String ruc);

    boolean existsByRuc(String ruc);
}
