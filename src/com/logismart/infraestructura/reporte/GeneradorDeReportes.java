package com.logismart.infraestructura.reporte;

import com.logismart.dominio.empresa.Reporte;

/**
 * GRASP Protected Variations: Abstrae el formato de exportacion de reportes.
 * Punto de variacion: el formato puede ser PDF, Excel, JSON o CSV.
 * ReporteService no necesita saber el formato; solo invoca exportar().
 */
public interface GeneradorDeReportes {

    /**
     * Exporta el reporte al formato que implementa esta clase.
     *
     * @param reporte el reporte a exportar
     * @return representacion del reporte en el formato elegido (ruta del archivo, contenido, etc.)
     */
    String exportar(Reporte reporte);

    /**
     * Extension del archivo generado (pdf, json, xlsx, csv).
     */
    String getFormato();
}

