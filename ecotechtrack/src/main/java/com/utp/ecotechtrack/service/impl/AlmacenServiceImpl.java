package com.utp.ecotechtrack.service.impl;

import com.utp.ecotechtrack.dto.AlmacenDTO;
import com.utp.ecotechtrack.exception.BadRequestException;
import com.utp.ecotechtrack.exception.ResourceNotFoundException;
import com.utp.ecotechtrack.model.Almacen;
import com.utp.ecotechtrack.repository.AlmacenRepository;
import com.utp.ecotechtrack.service.AlmacenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlmacenServiceImpl implements AlmacenService {

    private final AlmacenRepository almacenRepository;

    public AlmacenServiceImpl(AlmacenRepository almacenRepository) {
        this.almacenRepository = almacenRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AlmacenDTO> listarTodos() {
        return almacenRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AlmacenDTO obtenerPorId(Long id) {
        Almacen entidad = almacenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el almacén con ID: " + id));
        return convertirADTO(entidad);
    }

    @Override
    @Transactional
    public AlmacenDTO registrar(AlmacenDTO dto) {
        if (almacenRepository.existsByCodigo(dto.getCodigo().trim())) {
            throw new BadRequestException("Ya existe un almacén con el código: " + dto.getCodigo());
        }

        Almacen entidad = Almacen.builder()
                .codigo(dto.getCodigo().trim().toUpperCase())
                .nombre(dto.getNombre().trim())
                .sede(dto.getSede().trim())
                .direccion(dto.getDireccion() != null ? dto.getDireccion().trim() : null)
                .capacidadKg(dto.getCapacidadKg())
                .responsable(dto.getResponsable() != null ? dto.getResponsable().trim() : null)
                .build();

        Almacen guardado = almacenRepository.save(entidad);
        return convertirADTO(guardado);
    }

    @Override
    @Transactional
    public AlmacenDTO actualizar(Long id, AlmacenDTO dto) {
        Almacen entidad = almacenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el almacén con ID: " + id));

        if (!entidad.getCodigo().equalsIgnoreCase(dto.getCodigo().trim()) && almacenRepository.existsByCodigo(dto.getCodigo().trim())) {
            throw new BadRequestException("El código " + dto.getCodigo() + " ya está en uso por otro almacén.");
        }

        entidad.setCodigo(dto.getCodigo().trim().toUpperCase());
        entidad.setNombre(dto.getNombre().trim());
        entidad.setSede(dto.getSede().trim());
        entidad.setDireccion(dto.getDireccion() != null ? dto.getDireccion().trim() : null);
        entidad.setCapacidadKg(dto.getCapacidadKg());
        entidad.setResponsable(dto.getResponsable() != null ? dto.getResponsable().trim() : null);

        Almacen actualizado = almacenRepository.save(entidad);
        return convertirADTO(actualizado);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        Almacen entidad = almacenRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el almacén con ID: " + id));
        almacenRepository.delete(entidad);
    }

    private AlmacenDTO convertirADTO(Almacen entity) {
        return AlmacenDTO.builder()
                .id(entity.getId())
                .codigo(entity.getCodigo())
                .nombre(entity.getNombre())
                .sede(entity.getSede())
                .direccion(entity.getDireccion())
                .capacidadKg(entity.getCapacidadKg())
                .responsable(entity.getResponsable())
                .totalResiduosAlmacenados(entity.getResiduos() != null ? entity.getResiduos().size() : 0)
                .build();
    }
}
