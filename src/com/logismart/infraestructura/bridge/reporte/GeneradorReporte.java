package com.logismart.infraestructura.bridge.reporte;

public interface GeneradorReporte {
    String formatear(String contenido);
    String obtenerExtension();
}
