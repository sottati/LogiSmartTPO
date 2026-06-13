package com.logismart.infraestructura.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;

public class DashboardObservador implements ObservadorEnvio {
    @Override
    public void actualizar(Envio envio) {
        System.out.println("[Dashboard] Actualizando métricas — envío " + envio.getId()
                + " → " + envio.getEstado());
    }
}
