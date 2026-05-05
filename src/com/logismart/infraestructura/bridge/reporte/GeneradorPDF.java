package com.logismart.infraestructura.bridge.reporte;

public class GeneradorPDF implements GeneradorReporte {
    @Override
    public String formatear(String contenido) {
        return "%PDF-1.4\n" + contenido + "\n%%EOF";
    }

    @Override
    public String obtenerExtension() {
        return "pdf";
    }
}
