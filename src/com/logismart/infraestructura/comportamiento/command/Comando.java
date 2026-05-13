package com.logismart.infraestructura.comportamiento.command;

public interface Comando {
    void ejecutar();
    void deshacer();
    String obtenerDescripcion();
}
