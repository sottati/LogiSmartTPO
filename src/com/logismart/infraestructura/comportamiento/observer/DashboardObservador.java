package com.logismart.infraestructura.comportamiento.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;

/**
 * Observador - Dashboard en tiempo real.
 * Refresca la vista del panel de operaciones logísticas al detectar
 * cualquier cambio de estado en un envío suscrito.
 *
 * Patrón: Observer (GoF) - Hito 11
 */
public class DashboardObservador implements ObservadorEnvio {

    @Override
    public void actualizar(Envio envio) {
        System.out.println("[Dashboard-Obs] Actualizando panel - envío "
                + envio.getId() + ": " + envio.obtenerEstado());
    }
}

