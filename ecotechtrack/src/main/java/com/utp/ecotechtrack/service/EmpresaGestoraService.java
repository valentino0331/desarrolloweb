package com.utp.ecotechtrack.service;

import com.utp.ecotechtrack.dto.EmpresaGestoraDTO;

import java.util.List;

public interface EmpresaGestoraService {

    List<EmpresaGestoraDTO> listarTodas();

    EmpresaGestoraDTO obtenerPorId(Long id);

    EmpresaGestoraDTO registrar(EmpresaGestoraDTO dto);

    EmpresaGestoraDTO actualizar(Long id, EmpresaGestoraDTO dto);

    void eliminar(Long id);
}
