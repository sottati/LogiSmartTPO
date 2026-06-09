package com.logismart.infraestructura.comportamiento.visitor;

import com.logismart.infraestructura.estructural.composite.centro.CentroRegional;
import com.logismart.infraestructura.estructural.composite.centro.SucursalEntrega;

public class VisitorCalculoCostoOperativo implements VisitorCentro {
    private double costoTotal;
    private int nodosVisitados;

    @Override
    public void visitar(SucursalEntrega punto) {
        costoTotal += punto.obtenerCostoOperativo();
        nodosVisitados++;
    }

    @Override
    public void visitar(CentroRegional centro) {
        costoTotal += 1000.0;
        nodosVisitados++;
    }

    public double obtenerCostoTotal() {
        return costoTotal;
    }

    public int obtenerNodosVisitados() {
        return nodosVisitados;
    }
}
