package com.logismart.infraestructura.bridge;

import com.logismart.dominio.envio.Envio;
import java.util.List;

public class ReporteEnvios extends Reporte {
    private final List<Envio> envios;

    public ReporteEnvios(GeneradorReporte generador, List<Envio> envios) {
        super(generador);
        this.envios = envios;
    }

    @Override
    public String generarContenido() {
        StringBuilder sb = new StringBuilder("=== REPORTE DE ENVIOS ===\nTotal: ").append(envios.size()).append("\n");
        for (Envio e : envios) {
            sb.append(e.getId()).append(" | ").append(e.getOrigen()).append(" → ")
              .append(e.getDestino()).append(" | ").append(e.getEstado()).append("\n");
        }
        return sb.toString();
    }
}
