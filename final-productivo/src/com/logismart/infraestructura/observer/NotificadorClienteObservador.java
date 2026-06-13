package com.logismart.infraestructura.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;

public class NotificadorClienteObservador implements ObservadorEnvio {
    @Override
    public void actualizar(Envio envio) {
        System.out.println("[Notif-Cliente] Envío " + envio.getId()
                + " → estado: " + envio.getEstado() + " (SMS/email al cliente)");
    }
}
