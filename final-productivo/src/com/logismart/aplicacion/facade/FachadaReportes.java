package com.logismart.aplicacion.facade;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.bridge.GeneradorCSV;
import com.logismart.infraestructura.bridge.GeneradorExcel;
import com.logismart.infraestructura.bridge.GeneradorJSON;
import com.logismart.infraestructura.bridge.GeneradorPDF;
import com.logismart.infraestructura.bridge.GeneradorReporte;
import com.logismart.infraestructura.bridge.Reporte;
import com.logismart.infraestructura.bridge.ReporteDesempeno;
import com.logismart.infraestructura.bridge.ReporteEnvios;
import com.logismart.infraestructura.bridge.ReporteIngresos;
import com.logismart.infraestructura.singleton.Logger;

import java.util.List;

/**
 * Facade sobre el subsistema Bridge de reportes internos.
 * Traduce los parámetros tipo+formato en los objetos concretos del Bridge
 * (Reporte × GeneradorReporte) sin exponer esas clases al Controller.
 * Pertenece a la capa Aplicación — el Controller es el único cliente directo.
 */
public class FachadaReportes {

    private final Logger log = Logger.obtenerInstancia();

    // ── Reportes (Bridge) — matriz 3 tipos × 4 formatos ──────────────────────

    /** Genera un reporte del tipo y formato indicados.
     *  tipo:   ENVIOS (default) | INGRESOS | DESEMPENO
     *  formato: PDF (default)   | JSON | EXCEL | CSV */
    public String generarReporte(List<Envio> envios, String tipo, String formato) {
        GeneradorReporte gen = seleccionarGenerador(formato);
        Reporte reporte;
        switch (tipo == null ? "" : tipo.toUpperCase()) {
            case "INGRESOS":  reporte = new ReporteIngresos(gen, envios);  break;
            case "DESEMPENO": reporte = new ReporteDesempeno(gen, envios); break;
            default:          reporte = new ReporteEnvios(gen, envios);    break;
        }
        String resultado = reporte.generar();
        log.log("FachadaReportes: tipo=" + tipo + " formato=" + formato + " — " + envios.size() + " envios");
        return resultado;
    }

    private GeneradorReporte seleccionarGenerador(String formato) {
        if (formato == null) return new GeneradorPDF();
        switch (formato.toUpperCase()) {
            case "JSON":  return new GeneradorJSON();
            case "EXCEL": return new GeneradorExcel();
            case "CSV":   return new GeneradorCSV();
            default:      return new GeneradorPDF();
        }
    }
}
