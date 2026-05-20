package com.logismart.infraestructura.comportamiento.mediator;

/**
 * Interfaz Mediator - contrato del coordinador central.
 *
 * Los componentes del sistema (CentroDistribucion, ValidadorEnvio,
 * SistemaPago, SistemaNotificacion, SistemaAuditoria) se registran
 * aquí y se comunican exclusivamente a través del mediador,
 * eliminando las dependencias directas entre ellos.
 *
 * Patrón: Mediator (GoF) - Hito 11
 */
public interface MediadorEnvios {

    void registrarCentro(CentroDistribucion centro);
    void registrarValidador(ValidadorEnvio validador);
    void registrarPago(SistemaPago pago);
    void registrarNotificador(SistemaNotificacion notificador);
    void registrarAuditoria(SistemaAuditoria auditoria);

    /**
     * Punto de entrada para cualquier evento del sistema.
     * El mediador decide qué componente actúa según el nombre del evento.
     *
     * @param evento nombre del evento (ej. "ENVIO_CREADO")
     * @param datos  payload asociado al evento (típicamente un Envio)
     */
    void notificar(String evento, Object datos);
}
