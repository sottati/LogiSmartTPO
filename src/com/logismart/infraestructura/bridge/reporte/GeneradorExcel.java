package com.logismart.infraestructura.bridge.reporte;

public class GeneradorExcel implements GeneradorReporte {
    @Override
    public String formatear(String contenido) {
        return "<?xml version=\"1.0\"?>\n<Workbook>\n" + contenido + "\n</Workbook>";
    }

    @Override
    public String obtenerExtension() {
        return "xlsx";
    }
}
