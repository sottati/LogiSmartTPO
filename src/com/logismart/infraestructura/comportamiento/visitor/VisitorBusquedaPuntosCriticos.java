package com.logismart.infraestructura.comportamiento.visitor;

import com.logismart.infraestructura.estructural.composite.centro.CentroRegional;
import com.logismart.infraestructura.estructural.composite.centro.SucursalEntrega;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisitorBusquedaPuntosCriticos implements VisitorCentro {
    private final List<String> puntosCriticos = new ArrayList<>();
    private int nodosVisitados;

    @Override
    public void visitar(SucursalEntrega punto) {
        if (punto.obtenerCapacidad() > 0
                && punto.obtenerPaquetes() * 100.0 / punto.obtenerCapacidad() >= 90.0) {
            puntosCriticos.add(punto.obtenerNombre());
        }
        nodosVisitados++;
    }

    @Override
    public void visitar(CentroRegional centro) {
        nodosVisitados++;
    }

    public List<String> obtenerPuntosCriticos() {
        return Collections.unmodifiableList(puntosCriticos);
    }

    public int obtenerNodosVisitados() {
        return nodosVisitados;
    }
}
