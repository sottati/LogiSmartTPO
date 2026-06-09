package com.logismart.infraestructura.estructural.reporte;

import com.logismart.dominio.empresa.Reporte;

/**
 * GRASP Protected Variations: Exporta reportes en formato PDF.
 * ServicioReportes no necesita saber el formato; solo llama a exportar().
 * En produccion usaria una libreria como iText o JasperReports.
 */
public class GeneradorDeReportesPDF implements GeneradorDeReportes {

    @Override
    public String exportar(Reporte reporte) {
        if (reporte == null) {
            throw new IllegalArgumentException("El reporte no puede ser nulo");
        }
        // En produccion: generacion del PDF con libreria especializada
        String nombreArchivo = "reporte_" + reporte.getId() + ".pdf";
        System.out.printf("[PDF] Generando reporte %s -> %s%n", reporte.getId(), nombreArchivo);
        return nombreArchivo;
    }

    @Override
    public String getFormato() {
        return "pdf";
    }
}

