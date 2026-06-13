package com.logismart.aplicacion.cadena;

public abstract class ValidadorEnvio {
    protected ValidadorEnvio siguiente;

    public ValidadorEnvio setSiguiente(ValidadorEnvio siguiente) {
        this.siguiente = siguiente;
        return siguiente;
    }

    public abstract boolean validar(ContextoValidacion ctx);
    public abstract String  obtenerNombre();
}
