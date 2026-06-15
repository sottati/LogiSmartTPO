package com.logismart.infraestructura.observer;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;
import com.logismart.infraestructura.singleton.Logger;

public class NotificadorClienteObservador implements ObservadorEnvio {
    @Override
    public void actualizar(Envio envio) {
        Logger.obtenerInstancia().log("[Notif-Cliente] Envío " + envio.getId()
                + " → estado: " + envio.getEstado() + " (SMS/email al cliente)");
    }
}
