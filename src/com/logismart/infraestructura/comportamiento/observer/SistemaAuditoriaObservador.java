package com.logismart.infraestructura.comportamiento.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;

/**
 * Observador - Sistema de Auditoría.
 * Registra en el log de auditoría cada transición de estado de un envío.
 *
 * Patrón: Observer (GoF) - Hito 11
 */
public class SistemaAuditoriaObservador implements ObservadorEnvio {

    @Override
    public void actualizar(Envio envio) {
        System.out.println("[Auditoria-Obs] LOG: envio=" + envio.getId()
                + " | estado=" + envio.obtenerEstado()
                + " | ts=" + System.currentTimeMillis());
    }
}

