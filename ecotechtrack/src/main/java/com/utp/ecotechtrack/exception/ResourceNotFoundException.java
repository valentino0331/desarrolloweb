package com.utp.ecotechtrack.exception;

/**
 * Excepción lanzada cuando un recurso solicitado no existe en la base de datos (HTTP 404).
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }
}
