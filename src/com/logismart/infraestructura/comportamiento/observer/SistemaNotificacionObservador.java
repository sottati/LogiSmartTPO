package com.logismart.infraestructura.comportamiento.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;

/**
 * Observador - Sistema de Notificación al cliente.
 * Envía email/SMS al destinatario cada vez que el estado del envío cambia.
 *
 * Patrón: Observer (GoF) - Hito 11
 */
public class SistemaNotificacionObservador implements ObservadorEnvio {

    @Override
    public void actualizar(Envio envio) {
        System.out.println("[Notif-Obs] Enviando aviso al cliente - envío "
                + envio.getId() + " ahora en estado: " + envio.obtenerEstado());
    }
}

