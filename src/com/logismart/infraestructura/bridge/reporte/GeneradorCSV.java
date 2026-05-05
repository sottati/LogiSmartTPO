package com.logismart.infraestructura.bridge.reporte;

public class GeneradorCSV implements GeneradorReporte {
    @Override
    public String formatear(String contenido) {
        return contenido.replace("\n", "\r\n");
    }

    @Override
    public String obtenerExtension() {
        return "csv";
    }
}
