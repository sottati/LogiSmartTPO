package com.logismart.infraestructura.bridge;

import com.logismart.dominio.envio.Envio;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReporteDesempeno extends Reporte {
    private final List<Envio> envios;

    public ReporteDesempeno(GeneradorReporte generador, List<Envio> envios) {
        super(generador);
        this.envios = envios;
    }

    @Override
    public String generarContenido() {
        long total      = envios.size();
        long entregados = envios.stream().filter(e -> "ENTREGADO".equals(e.getEstado())).count();
        double pct      = total == 0 ? 0.0 : (entregados * 100.0 / total);
        double pesoAvg  = total == 0 ? 0.0 : envios.stream().mapToDouble(Envio::getPeso).average().orElse(0.0);
        Map<String, Long> porEstado = envios.stream()
                .collect(Collectors.groupingBy(Envio::getEstado, Collectors.counting()));

        StringBuilder sb = new StringBuilder("=== REPORTE DE DESEMPEÑO LOGÍSTICO ===\n")
                .append("Total envíos:    ").append(total).append("\n")
                .append("Entregados:      ").append(entregados)
                .append(" (").append(String.format("%.1f", pct)).append("%)\n")
                .append("Peso promedio:   ").append(String.format("%.2f", pesoAvg)).append(" kg\n")
                .append("Por estado:\n");
        porEstado.forEach((estado, cant) ->
                sb.append("  ").append(estado).append(": ").append(cant).append("\n"));
        return sb.toString();
    }
}
