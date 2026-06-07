package com.logismart.infraestructura.comportamiento.chain;

public abstract class ValidadorEnvio {
    protected ValidadorEnvio siguiente;

    public void setSiguiente(ValidadorEnvio siguiente) {
        this.siguiente = siguiente;
    }

    public abstract boolean validar(ContextoValidacion ctx);
    public abstract String obtenerNombre();
}

