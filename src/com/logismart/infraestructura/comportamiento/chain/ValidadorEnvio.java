package com.logismart.infraestructura.comportamiento.chain;

import com.logismart.dominio.envio.Envio;

public abstract class ValidadorEnvio {
    protected ValidadorEnvio siguiente;

    public void setSiguiente(ValidadorEnvio siguiente) {
        this.siguiente = siguiente;
    }

    public abstract boolean validar(Envio envio);
    public abstract String obtenerNombre();
}

