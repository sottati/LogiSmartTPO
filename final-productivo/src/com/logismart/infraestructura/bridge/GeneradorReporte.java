package com.logismart.infraestructura.bridge;

public interface GeneradorReporte {
    String formatear(String contenido);
    String obtenerExtension();
}
