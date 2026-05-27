package com.logismart.infraestructura.comportamiento.visitor;

public class VisitorCalculoCostoOperativo implements VisitorCentro {
    private double costoTotal;
    private int nodosVisitados;

    @Override
    public void visitar(NodoPuntoEntrega punto) {
        costoTotal += punto.obtenerCostoOperativo();
        nodosVisitados++;
    }

    @Override
    public void visitar(NodoCentroRegional centro) {
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
