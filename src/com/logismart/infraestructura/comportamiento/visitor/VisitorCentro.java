package com.logismart.infraestructura.comportamiento.visitor;

public interface VisitorCentro {
    void visitar(NodoPuntoEntrega punto);
    void visitar(NodoCentroRegional centro);
}
