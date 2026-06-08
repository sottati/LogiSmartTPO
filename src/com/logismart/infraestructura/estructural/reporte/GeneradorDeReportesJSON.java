package com.logismart.infraestructura.estructural.reporte;

import com.logismart.dominio.empresa.Reporte;

/**
 * GRASP Protected Variations: Exporta reportes en formato JSON.
 * Util para integraciones con sistemas externos o dashboards web.
 * En produccion usaria Jackson o Gson para serializar.
 */
public class GeneradorDeReportesJSON implements GeneradorDeReportes {

    @Override
    public String exportar(Reporte reporte) {
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte no puede ser nulo");
        }
        String json = String.format(
                "{\"id\":\"%s\",\"periodo\":\"%s\",\"tipo\":\"%s\",\"generadoEn\":\"%s\"}",
                reporte.getId(),
                reporte.getPeriodo(),
                reporte.getTipo(),
                reporte.getGeneradoEn() != null ? reporte.getGeneradoEn().toString() : "");
        System.out.printf("[JSON] Reporte %s serializado%n", reporte.getId());
        return json;
    }

    @Override
    public String getFormato() {
        return "json";
    }
}

