package com.logismart.infraestructura.comportamiento.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;

/**
 * Observador - Centro de Distribución.
 * Actualiza el tablero operativo cuando un envío cambia de estado.
 *
 * Patrón: Observer (GoF) - Hito 11
 */
public class CentroDistribucionObservador implements ObservadorEnvio {

    @Override
    public void actualizar(Envio envio) {
        System.out.println("[Centro-Obs] Envío " + envio.getId()
                + " → estado: " + envio.obtenerEstado());
    }
}

