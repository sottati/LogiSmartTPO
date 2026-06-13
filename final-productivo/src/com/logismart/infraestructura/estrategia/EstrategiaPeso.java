package com.logismart.infraestructura.estrategia;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstrategiaCalculoCosto;

public class EstrategiaPeso implements EstrategiaCalculoCosto {
    private final double tarifaPorKg;

    public EstrategiaPeso(double tarifaPorKg) { this.tarifaPorKg = tarifaPorKg; }
    public EstrategiaPeso() { this(5.0); }

    @Override
    public double calcular(Envio envio) {
        return envio.getPeso() * tarifaPorKg;
    }
}
