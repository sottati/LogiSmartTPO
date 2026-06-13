package com.logismart.infraestructura.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;

public class AuditoriaObservador implements ObservadorEnvio {
    @Override
    public void actualizar(Envio envio) {
        System.out.println("[Auditoria] " + System.currentTimeMillis()
                + " | envío=" + envio.getId() + " | estado=" + envio.getEstado());
    }
}
