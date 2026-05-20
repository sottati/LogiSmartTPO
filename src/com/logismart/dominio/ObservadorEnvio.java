package com.logismart.dominio;

/**
 * Puerto de salida del dominio - contrato Observer.
 *
 * Define qué debe hacer cualquier componente que quiera ser notificado
 * cuando un Envío cambia de estado. La interfaz vive en el paquete dominio
 * porque es el dominio quien la necesita (Dependency Inversion Principle):
 * el dominio define el contrato, la infraestructura lo implementa.
 *
 * Patrón: Observer (GoF) - Hito 11
 */
public interface ObservadorEnvio {

    /**
     * Invocado automáticamente cada vez que el Envío al que este observador
     * está suscrito cambia de estado.
     *
     * @param envio referencia al Envío que disparó el cambio
     */
    void actualizar(Envio envio);
}
