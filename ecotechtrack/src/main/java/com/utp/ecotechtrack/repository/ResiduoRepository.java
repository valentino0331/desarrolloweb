package com.utp.ecotechtrack.repository;

import com.utp.ecotechtrack.model.CategoriaRAEE;
import com.utp.ecotechtrack.model.EstadoResiduo;
import com.utp.ecotechtrack.model.ResiduoElectronico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para la entidad ResiduoElectronico.
 * Proporciona métodos CRUD automáticos y consultas por convención de nombres.
 */
@Repository
public interface ResiduoRepository extends JpaRepository<ResiduoElectronico, Long> {

    Optional<ResiduoElectronico> findByCodigoIdentificacion(String codigoIdentificacion);

    boolean existsByCodigoIdentificacion(String codigoIdentificacion);

    List<ResiduoElectronico> findByEstado(EstadoResiduo estado);

    List<ResiduoElectronico> findByCategoria(CategoriaRAEE categoria);

    List<ResiduoElectronico> findByAlmacenId(Long almacenId);

    List<ResiduoElectronico> findByEmpresaGestoraId(Long empresaGestoraId);
}
