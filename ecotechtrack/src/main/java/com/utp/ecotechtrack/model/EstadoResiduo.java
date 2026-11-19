package com.utp.ecotechtrack.model;

/**
 * Representa las etapas del ciclo de vida y trazabilidad del residuo electrónico (RAEE).
 */
public enum EstadoResiduo {
    DECLARADO_BAJA,         // El equipo ha sido dado de baja por la institución/empresa
    ALMACENADO_TEMPORAL,    // Almacenado en centro de acopio institucional
    SOLICITUD_RECOJO,       // Se ha generado una orden de recojo para transporte
    EN_TRANSITO,            // Transportista asignado y en traslado
    RECEPCIONADO_GESTORA,   // Recibido por la empresa de tratamiento y valorización
    EN_DESMANTELAMIENTO,    // En proceso de recuperación y reciclaje de partes
    DISPOSICION_FINAL       // Destino final seguro certificado
}
