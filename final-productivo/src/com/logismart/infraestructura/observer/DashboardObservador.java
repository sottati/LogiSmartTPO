package com.logismart.infraestructura.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;
import com.logismart.infraestructura.singleton.Logger;

public class DashboardObservador implements ObservadorEnvio {
    @Override
    public void actualizar(Envio envio) {
        Logger.obtenerInstancia().log("[Dashboard] Actualizando métricas — envío " + envio.getId()
                + " → " + envio.getEstado());
    }
}
