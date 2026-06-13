package com.logismart.infraestructura.bridge;

import com.logismart.dominio.envio.Envio;
import java.util.List;

public class ReporteIngresos extends Reporte {
    private final List<Envio> envios;

    public ReporteIngresos(GeneradorReporte generador, List<Envio> envios) {
        super(generador);
        this.envios = envios;
    }

    @Override
    public String generarContenido() {
        double total = envios.stream().mapToDouble(Envio::getCosto).sum();
        return "=== REPORTE DE INGRESOS ===\nEnvios: " + envios.size() + "\nTotal: $" + total;
    }
}
