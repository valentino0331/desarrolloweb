package com.utp.ecotechtrack.service.impl;

import com.utp.ecotechtrack.dto.*;
import com.utp.ecotechtrack.exception.BadRequestException;
import com.utp.ecotechtrack.exception.ResourceNotFoundException;
import com.utp.ecotechtrack.model.*;
import com.utp.ecotechtrack.repository.AlmacenRepository;
import com.utp.ecotechtrack.repository.EmpresaGestoraRepository;
import com.utp.ecotechtrack.repository.HistorialMovimientoRepository;
import com.utp.ecotechtrack.repository.ResiduoRepository;
import com.utp.ecotechtrack.service.ResiduoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación de la lógica de negocio para la trazabilidad de residuos electrónicos.
 * Integra relaciones con Almacen, EmpresaGestora y registro de HistorialMovimiento.
 */
@Service
public class ResiduoServiceImpl implements ResiduoService {

    private final ResiduoRepository residuoRepository;
    private final AlmacenRepository almacenRepository;
    private final EmpresaGestoraRepository empresaGestoraRepository;
    private final HistorialMovimientoRepository historialMovimientoRepository;

    // Inyección de dependencias por constructor
    public ResiduoServiceImpl(ResiduoRepository residuoRepository,
                              AlmacenRepository almacenRepository,
                              EmpresaGestoraRepository empresaGestoraRepository,
                              HistorialMovimientoRepository historialMovimientoRepository) {
        this.residuoRepository = residuoRepository;
        this.almacenRepository = almacenRepository;
        this.empresaGestoraRepository = empresaGestoraRepository;
        this.historialMovimientoRepository = historialMovimientoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResiduoResponseDTO> listarTodos() {
        return residuoRepository.findAll()
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ResiduoResponseDTO obtenerPorId(Long id) {
        ResiduoElectronico entidad = residuoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el residuo electrónico con ID: " + id));
        return convertirAResponseDTO(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public ResiduoResponseDTO obtenerPorCodigo(String codigoIdentificacion) {
        ResiduoElectronico entidad = residuoRepository.findByCodigoIdentificacion(codigoIdentificacion)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el residuo con código: " + codigoIdentificacion));
        return convertirAResponseDTO(entidad);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResiduoResponseDTO> listarPorEstado(EstadoResiduo estado) {
        return residuoRepository.findByEstado(estado)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResiduoResponseDTO> listarPorCategoria(CategoriaRAEE categoria) {
        return residuoRepository.findByCategoria(categoria)
                .stream()
                .map(this::convertirAResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResiduoResponseDTO registrar(ResiduoRequestDTO requestDTO) {
        if (residuoRepository.existsByCodigoIdentificacion(requestDTO.getCodigoIdentificacion())) {
            throw new BadRequestException("Ya existe un residuo registrado con el código: " + requestDTO.getCodigoIdentificacion());
        }

        // Resolver relación con Almacén
        Almacen almacen = null;
        if (requestDTO.getAlmacenId() != null) {
            almacen = almacenRepository.findById(requestDTO.getAlmacenId())
                    .orElseThrow(() -> new ResourceNotFoundException("No existe el almacén con ID: " + requestDTO.getAlmacenId()));
        }

        // Resolver relación con Empresa Gestora
        EmpresaGestora empresaGestora = null;
        if (requestDTO.getEmpresaGestoraId() != null) {
            empresaGestora = empresaGestoraRepository.findById(requestDTO.getEmpresaGestoraId())
                    .orElseThrow(() -> new ResourceNotFoundException("No existe la empresa gestora con ID: " + requestDTO.getEmpresaGestoraId()));
        }

        EstadoResiduo estadoInicial = requestDTO.getEstado() != null ? requestDTO.getEstado() : EstadoResiduo.DECLARADO_BAJA;

        ResiduoElectronico entidad = ResiduoElectronico.builder()
                .codigoIdentificacion(requestDTO.getCodigoIdentificacion().trim().toUpperCase())
                .descripcion(requestDTO.getDescripcion().trim())
                .categoria(requestDTO.getCategoria())
                .marca(requestDTO.getMarca().trim())
                .modelo(requestDTO.getModelo().trim())
                .numeroSerie(requestDTO.getNumeroSerie() != null ? requestDTO.getNumeroSerie().trim() : null)
                .pesoKg(requestDTO.getPesoKg())
                .estado(estadoInicial)
                .almacen(almacen)
                .empresaGestora(empresaGestora)
                .observaciones(requestDTO.getObservaciones() != null ? requestDTO.getObservaciones().trim() : null)
                .build();

        ResiduoElectronico guardado = residuoRepository.save(entidad);

        // Registro de evento inicial en la cadena de trazabilidad
        HistorialMovimiento eventoInicial = HistorialMovimiento.builder()
                .residuo(guardado)
                .estadoAnterior(null)
                .estadoNuevo(estadoInicial)
                .fechaMovimiento(LocalDateTime.now())
                .responsable("Sistema / Registro Inicial")
                .observaciones("Declaración de baja técnica y registro inicial en EcoTechTrack")
                .build();

        historialMovimientoRepository.save(eventoInicial);

        return convertirAResponseDTO(guardado);
    }

    @Override
    @Transactional
    public ResiduoResponseDTO actualizar(Long id, ResiduoRequestDTO requestDTO) {
        ResiduoElectronico entidad = residuoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el residuo electrónico con ID: " + id));

        if (!entidad.getCodigoIdentificacion().equalsIgnoreCase(requestDTO.getCodigoIdentificacion()) &&
                residuoRepository.existsByCodigoIdentificacion(requestDTO.getCodigoIdentificacion())) {
            throw new BadRequestException("El código " + requestDTO.getCodigoIdentificacion() + " ya está en uso por otro equipo.");
        }

        entidad.setCodigoIdentificacion(requestDTO.getCodigoIdentificacion().trim().toUpperCase());
        entidad.setDescripcion(requestDTO.getDescripcion().trim());
        entidad.setCategoria(requestDTO.getCategoria());
        entidad.setMarca(requestDTO.getMarca().trim());
        entidad.setModelo(requestDTO.getModelo().trim());
        entidad.setNumeroSerie(requestDTO.getNumeroSerie() != null ? requestDTO.getNumeroSerie().trim() : null);
        entidad.setPesoKg(requestDTO.getPesoKg());

        if (requestDTO.getEstado() != null) {
            entidad.setEstado(requestDTO.getEstado());
        }

        if (requestDTO.getAlmacenId() != null) {
            Almacen alm = almacenRepository.findById(requestDTO.getAlmacenId())
                    .orElseThrow(() -> new ResourceNotFoundException("No existe el almacén con ID: " + requestDTO.getAlmacenId()));
            entidad.setAlmacen(alm);
        }

        if (requestDTO.getEmpresaGestoraId() != null) {
            EmpresaGestora emp = empresaGestoraRepository.findById(requestDTO.getEmpresaGestoraId())
                    .orElseThrow(() -> new ResourceNotFoundException("No existe la empresa gestora con ID: " + requestDTO.getEmpresaGestoraId()));
            entidad.setEmpresaGestora(emp);
        }

        entidad.setObservaciones(requestDTO.getObservaciones());

        ResiduoElectronico actualizado = residuoRepository.save(entidad);
        return convertirAResponseDTO(actualizado);
    }

    @Override
    @Transactional
    public ResiduoResponseDTO cambiarEstado(Long id, CambioEstadoDTO cambioEstadoDTO) {
        ResiduoElectronico entidad = residuoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el residuo electrónico con ID: " + id));

        EstadoResiduo estadoAnterior = entidad.getEstado();
        entidad.setEstado(cambioEstadoDTO.getNuevoEstado());

        if (cambioEstadoDTO.getObservaciones() != null && !cambioEstadoDTO.getObservaciones().isBlank()) {
            String anterior = entidad.getObservaciones() != null ? entidad.getObservaciones() + " | " : "";
            entidad.setObservaciones(anterior + cambioEstadoDTO.getObservaciones().trim());
        }

        ResiduoElectronico actualizado = residuoRepository.save(entidad);

        // Guardar hito en el historial de trazabilidad
        HistorialMovimiento movimiento = HistorialMovimiento.builder()
                .residuo(actualizado)
                .estadoAnterior(estadoAnterior)
                .estadoNuevo(cambioEstadoDTO.getNuevoEstado())
                .fechaMovimiento(LocalDateTime.now())
                .responsable("Operador RAEE")
                .observaciones(cambioEstadoDTO.getObservaciones() != null ? cambioEstadoDTO.getObservaciones().trim() : "Cambio de fase de trazabilidad")
                .build();

        historialMovimientoRepository.save(movimiento);

        return convertirAResponseDTO(actualizado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistorialMovimientoDTO> obtenerHistorial(Long residuoId) {
        if (!residuoRepository.existsById(residuoId)) {
            throw new ResourceNotFoundException("No se encontró el residuo con ID: " + residuoId);
        }
        return historialMovimientoRepository.findByResiduoIdOrderByFechaMovimientoDesc(residuoId)
                .stream()
                .map(h -> HistorialMovimientoDTO.builder()
                        .id(h.getId())
                        .residuoId(residuoId)
                        .codigoResiduo(h.getResiduo() != null ? h.getResiduo().getCodigoIdentificacion() : null)
                        .estadoAnterior(h.getEstadoAnterior())
                        .estadoNuevo(h.getEstadoNuevo())
                        .fechaMovimiento(h.getFechaMovimiento())
                        .responsable(h.getResponsable())
                        .observaciones(h.getObservaciones())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        ResiduoElectronico entidad = residuoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se puede eliminar: no existe el residuo con ID: " + id));
        residuoRepository.delete(entidad);
    }

    private ResiduoResponseDTO convertirAResponseDTO(ResiduoElectronico entity) {
        Long almId = entity.getAlmacen() != null ? entity.getAlmacen().getId() : null;
        String ubicacionTexto = entity.getAlmacen() != null
                ? entity.getAlmacen().getNombre() + " (" + entity.getAlmacen().getSede() + ")"
                : "Sin almacén asignado";

        AlmacenDTO almDTO = null;
        if (entity.getAlmacen() != null) {
            almDTO = AlmacenDTO.builder()
                    .id(entity.getAlmacen().getId())
                    .codigo(entity.getAlmacen().getCodigo())
                    .nombre(entity.getAlmacen().getNombre())
                    .sede(entity.getAlmacen().getSede())
                    .direccion(entity.getAlmacen().getDireccion())
                    .capacidadKg(entity.getAlmacen().getCapacidadKg())
                    .responsable(entity.getAlmacen().getResponsable())
                    .build();
        }

        Long empId = entity.getEmpresaGestora() != null ? entity.getEmpresaGestora().getId() : null;
        String empTexto = entity.getEmpresaGestora() != null
                ? entity.getEmpresaGestora().getRazonSocial()
                : "Pendiente de asignación";

        EmpresaGestoraDTO empDTO = null;
        if (entity.getEmpresaGestora() != null) {
            empDTO = EmpresaGestoraDTO.builder()
                    .id(entity.getEmpresaGestora().getId())
                    .ruc(entity.getEmpresaGestora().getRuc())
                    .razonSocial(entity.getEmpresaGestora().getRazonSocial())
                    .registroAutorizacion(entity.getEmpresaGestora().getRegistroAutorizacion())
                    .email(entity.getEmpresaGestora().getEmail())
                    .telefono(entity.getEmpresaGestora().getTelefono())
                    .direccion(entity.getEmpresaGestora().getDireccion())
                    .build();
        }

        return ResiduoResponseDTO.builder()
                .id(entity.getId())
                .codigoIdentificacion(entity.getCodigoIdentificacion())
                .descripcion(entity.getDescripcion())
                .categoria(entity.getCategoria())
                .marca(entity.getMarca())
                .modelo(entity.getModelo())
                .numeroSerie(entity.getNumeroSerie())
                .pesoKg(entity.getPesoKg())
                .estado(entity.getEstado())
                .almacenId(almId)
                .ubicacionAlmacen(ubicacionTexto)
                .almacen(almDTO)
                .empresaGestoraId(empId)
                .empresaGestora(empTexto)
                .empresaGestoraDetalle(empDTO)
                .observaciones(entity.getObservaciones())
                .fechaRegistro(entity.getFechaRegistro())
                .fechaActualizacion(entity.getFechaActualizacion())
                .build();
    }
}
