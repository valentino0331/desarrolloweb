package com.utp.ecotechtrack.repository;

import com.utp.ecotechtrack.model.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {

    Optional<Almacen> findByCodigo(String codigo);

    boolean existsByCodigo(String codigo);

    /**
     * Consulta JPQL para filtrar almacenes por coincidencia en la sede.
     */
    @Query("SELECT a FROM Almacen a WHERE LOWER(a.sede) LIKE LOWER(CONCAT('%', :sede, '%'))")
    List<Almacen> findBySedeJPQL(@Param("sede") String sede);
}
