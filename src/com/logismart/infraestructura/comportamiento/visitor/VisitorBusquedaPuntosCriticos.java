package com.logismart.infraestructura.comportamiento.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisitorBusquedaPuntosCriticos implements VisitorCentro {
    private final List<String> puntosCriticos = new ArrayList<>();
    private int nodosVisitados;

    @Override
    public void visitar(NodoPuntoEntrega punto) {
        if (punto.obtenerCapacidad() > 0
                && punto.obtenerPaquetes() * 100.0 / punto.obtenerCapacidad() >= 90.0) {
            puntosCriticos.add(punto.obtenerNombre());
        }
        nodosVisitados++;
    }

    @Override
    public void visitar(NodoCentroRegional centro) {
        nodosVisitados++;
    }

    public List<String> obtenerPuntosCriticos() {
        return Collections.unmodifiableList(puntosCriticos);
    }

    public int obtenerNodosVisitados() {
        return nodosVisitados;
    }
}
