package com.logismart.infraestructura.estructural.bridge.reporte;

import java.util.Map;

public class ReporteDesempenoProveedores extends Reporte {
    private final Map<String, Integer> desempenoProveedores;

    public ReporteDesempenoProveedores(GeneradorReporte generador, Map<String, Integer> desempenoProveedores) {
        super(generador);
        this.desempenoProveedores = desempenoProveedores;
    }

    @Override
    public String generarContenido() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== REPORTE DE DESEMPENO DE PROVEEDORES ===\n");
        for (Map.Entry<String, Integer> entry : desempenoProveedores.entrySet()) {
            sb.append("Proveedor: ").append(entry.getKey()).append("\n");
            sb.append("Envios completados: ").append(entry.getValue()).append("\n");
            sb.append("---\n");
        }
        return sb.toString();
    }
}
