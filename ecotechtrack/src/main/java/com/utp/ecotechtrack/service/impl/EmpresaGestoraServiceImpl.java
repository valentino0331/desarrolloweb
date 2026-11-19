package com.utp.ecotechtrack.service.impl;

import com.utp.ecotechtrack.dto.EmpresaGestoraDTO;
import com.utp.ecotechtrack.exception.BadRequestException;
import com.utp.ecotechtrack.exception.ResourceNotFoundException;
import com.utp.ecotechtrack.model.EmpresaGestora;
import com.utp.ecotechtrack.repository.EmpresaGestoraRepository;
import com.utp.ecotechtrack.service.EmpresaGestoraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpresaGestoraServiceImpl implements EmpresaGestoraService {

    private final EmpresaGestoraRepository empresaGestoraRepository;

    public EmpresaGestoraServiceImpl(EmpresaGestoraRepository empresaGestoraRepository) {
        this.empresaGestoraRepository = empresaGestoraRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpresaGestoraDTO> listarTodas() {
        return empresaGestoraRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EmpresaGestoraDTO obtenerPorId(Long id) {
        EmpresaGestora entidad = empresaGestoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la empresa gestora con ID: " + id));
        return convertirADTO(entidad);
    }

    @Override
    @Transactional
    public EmpresaGestoraDTO registrar(EmpresaGestoraDTO dto) {
        if (empresaGestoraRepository.existsByRuc(dto.getRuc().trim())) {
            throw new BadRequestException("Ya existe una empresa gestora registrada con el RUC: " + dto.getRuc());
        }

        EmpresaGestora entidad = EmpresaGestora.builder()
                .ruc(dto.getRuc().trim())
                .razonSocial(dto.getRazonSocial().trim())
                .registroAutorizacion(dto.getRegistroAutorizacion().trim())
                .email(dto.getEmail() != null ? dto.getEmail().trim() : null)
                .telefono(dto.getTelefono() != null ? dto.getTelefono().trim() : null)
                .direccion(dto.getDireccion() != null ? dto.getDireccion().trim() : null)
                .build();

        EmpresaGestora guardada = empresaGestoraRepository.save(entidad);
        return convertirADTO(guardada);
    }

    @Override
    @Transactional
    public EmpresaGestoraDTO actualizar(Long id, EmpresaGestoraDTO dto) {
        EmpresaGestora entidad = empresaGestoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la empresa gestora con ID: " + id));

        if (!entidad.getRuc().equals(dto.getRuc().trim()) && empresaGestoraRepository.existsByRuc(dto.getRuc().trim())) {
            throw new BadRequestException("El RUC " + dto.getRuc() + " ya está asignado a otra empresa.");
        }

        entidad.setRuc(dto.getRuc().trim());
        entidad.setRazonSocial(dto.getRazonSocial().trim());
        entidad.setRegistroAutorizacion(dto.getRegistroAutorizacion().trim());
        entidad.setEmail(dto.getEmail() != null ? dto.getEmail().trim() : null);
        entidad.setTelefono(dto.getTelefono() != null ? dto.getTelefono().trim() : null);
        entidad.setDireccion(dto.getDireccion() != null ? dto.getDireccion().trim() : null);

        EmpresaGestora actualizada = empresaGestoraRepository.save(entidad);
        return convertirADTO(actualizada);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        EmpresaGestora entidad = empresaGestoraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró la empresa gestora con ID: " + id));
        empresaGestoraRepository.delete(entidad);
    }

    private EmpresaGestoraDTO convertirADTO(EmpresaGestora entity) {
        return EmpresaGestoraDTO.builder()
                .id(entity.getId())
                .ruc(entity.getRuc())
                .razonSocial(entity.getRazonSocial())
                .registroAutorizacion(entity.getRegistroAutorizacion())
                .email(entity.getEmail())
                .telefono(entity.getTelefono())
                .direccion(entity.getDireccion())
                .totalResiduosAsignados(entity.getResiduos() != null ? entity.getResiduos().size() : 0)
                .build();
    }
}
