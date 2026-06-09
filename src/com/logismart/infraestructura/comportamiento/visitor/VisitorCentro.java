package com.logismart.infraestructura.comportamiento.visitor;

import com.logismart.infraestructura.estructural.composite.centro.CentroRegional;
import com.logismart.infraestructura.estructural.composite.centro.SucursalEntrega;

public interface VisitorCentro {
    void visitar(SucursalEntrega punto);
    void visitar(CentroRegional centro);
}
