package com.logismart.infraestructura.bridge.reporte;

import com.logismart.dominio.Envio;
import java.util.List;

public class ReporteEnvios extends Reporte {
    private final List<Envio> envios;

    public ReporteEnvios(GeneradorReporte generador, List<Envio> envios) {
        super(generador);
        this.envios = envios;
    }

    @Override
    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE ENVIOS ===\n");
        sb.append("Total de envios: ").append(envios.size()).append("\n\n");
        for (Envio envio : envios) {
            sb.append("Id: ").append(envio.getId()).append("\n");
            sb.append("Origen: ").append(envio.getOrigen()).append("\n");
            sb.append("Destino: ").append(envio.getDestino()).append("\n");
            sb.append("Estado: ").append(envio.getEstado()).append("\n");
            sb.append("Peso: ").append(envio.getPeso()).append("\n");
            sb.append("---\n");
        }
        return sb.toString();
    }
}
