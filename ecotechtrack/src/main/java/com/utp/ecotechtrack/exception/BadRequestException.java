package com.utp.ecotechtrack.exception;

/**
 * Excepción lanzada cuando los datos de la solicitud son inválidos a nivel de negocio (HTTP 400).
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String mensaje) {
        super(mensaje);
    }
}
