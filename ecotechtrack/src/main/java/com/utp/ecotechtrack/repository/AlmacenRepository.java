package com.utp.ecotechtrack.repository;

import com.utp.ecotechtrack.model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {

    Optional<Almacen> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);
}
