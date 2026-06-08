package com.logismart.infraestructura.estructural.bridge.reporte;

public interface GeneradorReporte {
    String formatear(String contenido);
    String obtenerExtension();
}
