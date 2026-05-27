package com.logismart.infraestructura.comportamiento.visitor;

public class VisitorCalculoOcupacion implements VisitorCentro {
    private int paquetesTotales;
    private int capacidadTotal;
    private int nodosVisitados;

    @Override
    public void visitar(NodoPuntoEntrega punto) {
        paquetesTotales += punto.obtenerPaquetes();
        capacidadTotal += punto.obtenerCapacidad();
        nodosVisitados++;
    }

    @Override
    public void visitar(NodoCentroRegional centro) {
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
