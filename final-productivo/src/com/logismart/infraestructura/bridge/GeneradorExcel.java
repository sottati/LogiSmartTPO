package com.logismart.infraestructura.bridge;

public class GeneradorExcel implements GeneradorReporte {
    @Override
    public String formatear(String contenido) {
        String escaped = contenido.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
        return "<?xml version=\"1.0\"?><Workbook><Worksheet><Table>"
             + "<Row><Cell><Data>" + escaped + "</Data></Cell></Row>"
             + "</Table></Worksheet></Workbook>";
    }
    @Override public String obtenerExtension() { return "xlsx"; }
}
