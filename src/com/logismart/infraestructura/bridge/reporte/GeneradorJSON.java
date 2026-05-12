package com.logismart.infraestructura.bridge.reporte;

public class GeneradorJSON implements GeneradorReporte {
    @Override
    public String formatear(String contenido) {
        return "{\"reporte\": \"" + contenido.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"") + "\"}";
    }

    @Override
    public String obtenerExtension() {
        return "json";
    }
}
