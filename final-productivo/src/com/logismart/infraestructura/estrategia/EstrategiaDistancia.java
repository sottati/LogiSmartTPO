package com.logismart.infraestructura.estrategia;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstrategiaCalculoCosto;

public class EstrategiaDistancia implements EstrategiaCalculoCosto {
    private final double tarifaPorKm;

    public EstrategiaDistancia(double tarifaPorKm) { this.tarifaPorKm = tarifaPorKm; }
    public EstrategiaDistancia() { this(1.5); }

    @Override
    public double calcular(Envio envio) {
        return envio.getPeso() * 500.0 * tarifaPorKm;
    }
}
