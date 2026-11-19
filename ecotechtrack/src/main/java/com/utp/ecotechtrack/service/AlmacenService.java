package com.utp.ecotechtrack.service;

import com.utp.ecotechtrack.dto.AlmacenDTO;

import java.util.List;

public interface AlmacenService {

    List<AlmacenDTO> listarTodos();

    AlmacenDTO obtenerPorId(Long id);

    AlmacenDTO registrar(AlmacenDTO dto);

    AlmacenDTO actualizar(Long id, AlmacenDTO dto);

    void eliminar(Long id);
}
