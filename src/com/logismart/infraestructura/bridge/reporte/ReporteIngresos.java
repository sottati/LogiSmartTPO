package com.logismart.infraestructura.bridge.reporte;

import com.logismart.dominio.Envio;
import java.util.List;

public class ReporteIngresos extends Reporte {
    private final List<Envio> envios;

    public ReporteIngresos(GeneradorReporte generador, List<Envio> envios) {
        super(generador);
        this.envios = envios;
    }

    @Override
    public String generarContenido() {
        double total = 0;
        for (Envio envio : envios) {
            total += envio.getPeso() * 1200;
        }
        double promedio = envios.isEmpty() ? 0 : total / envios.size();
        return "=== REPORTE DE INGRESOS ===\n"
                + "Total de envios: " + envios.size() + "\n"
                + "Ingresos totales: $" + total + "\n"
                + "Promedio por envio: $" + promedio + "\n";
    }
}
