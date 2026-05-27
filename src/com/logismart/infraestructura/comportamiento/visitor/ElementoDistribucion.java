package com.logismart.infraestructura.comportamiento.visitor;

public interface ElementoDistribucion {
    void aceptar(VisitorCentro visitor);
    String obtenerNombre();
}
