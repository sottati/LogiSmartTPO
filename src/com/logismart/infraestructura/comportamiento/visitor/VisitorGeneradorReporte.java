package com.logismart.infraestructura.comportamiento.visitor;

import com.logismart.infraestructura.estructural.composite.centro.CentroRegional;
import com.logismart.infraestructura.estructural.composite.centro.SucursalEntrega;

public class VisitorGeneradorReporte implements VisitorCentro {
    private final StringBuilder reporte = new StringBuilder();
    private int nodosVisitados;

    @Override
    public void visitar(SucursalEntrega punto) {
        reporte.append("Punto: ")
                .append(punto.obtenerNombre())
                .append(" paquetes=")
                .append(punto.obtenerPaquetes())
                .append('\n');
        nodosVisitados++;
    }

    @Override
    public void visitar(CentroRegional centro) {
        reporte.append("Centro: ")
                .append(centro.obtenerNombre())
                .append('\n');
        nodosVisitados++;
    }

    public String obtenerReporte() {
        return reporte.toString();
    }

    public int obtenerNodosVisitados() {
        return nodosVisitados;
    }
}
