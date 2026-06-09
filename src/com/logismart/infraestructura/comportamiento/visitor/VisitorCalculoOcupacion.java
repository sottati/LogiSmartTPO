package com.logismart.infraestructura.comportamiento.visitor;

import com.logismart.infraestructura.estructural.composite.centro.CentroRegional;
import com.logismart.infraestructura.estructural.composite.centro.SucursalEntrega;

public class VisitorCalculoOcupacion implements VisitorCentro {
    private int paquetesTotales;
    private int capacidadTotal;
    private int nodosVisitados;

    @Override
    public void visitar(SucursalEntrega punto) {
        paquetesTotales += punto.obtenerPaquetes();
        capacidadTotal += punto.obtenerCapacidad();
        nodosVisitados++;
    }

    @Override
    public void visitar(CentroRegional centro) {
        nodosVisitados++;
    }

    public double obtenerOcupacion() {
        if (capacidadTotal == 0) {
            return 0.0;
        }
        return (paquetesTotales * 100.0) / capacidadTotal;
    }

    public int obtenerNodosVisitados() {
        return nodosVisitados;
    }
}
