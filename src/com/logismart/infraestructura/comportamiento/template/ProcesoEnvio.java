package com.logismart.infraestructura.comportamiento.template;

import com.logismart.dominio.Envio;

public abstract class ProcesoEnvio {
    public final void procesarEnvio(Envio envio) {
        validar(envio);
        calcularCosto(envio);
        procesarPago(envio);
        notificar(envio);
    }

    protected abstract void validar(Envio envio);
    protected abstract void calcularCosto(Envio envio);
    protected abstract void procesarPago(Envio envio);
    protected abstract void notificar(Envio envio);
}
