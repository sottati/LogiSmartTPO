package com.logismart.infraestructura.bridge;

public class GeneradorCSV implements GeneradorReporte {
    @Override
    public String formatear(String contenido) {
        StringBuilder sb = new StringBuilder();
        for (String linea : contenido.split("\n")) {
            sb.append("\"").append(linea.replace("\"", "\"\"")).append("\"\n");
        }
        return sb.toString();
    }
    @Override public String obtenerExtension() { return "csv"; }
}
