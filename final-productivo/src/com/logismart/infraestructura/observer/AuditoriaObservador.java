package com.logismart.infraestructura.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;
import com.logismart.infraestructura.singleton.Logger;

public class AuditoriaObservador implements ObservadorEnvio {
    @Override
    public void actualizar(Envio envio) {
        Logger.obtenerInstancia().log("[Auditoria] " + System.currentTimeMillis()
                + " | envío=" + envio.getId() + " | estado=" + envio.getEstado());
    }
}
