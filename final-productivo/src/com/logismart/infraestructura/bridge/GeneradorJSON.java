package com.logismart.infraestructura.bridge;

public class GeneradorJSON implements GeneradorReporte {
    @Override public String formatear(String contenido) { return "{\"reporte\":\"" + contenido.replace("\"", "'") + "\"}"; }
    @Override public String obtenerExtension()          { return "json"; }
}
