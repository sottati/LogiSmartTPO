package com.logismart.infraestructura.estrategia;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstrategiaCalculoCosto;

/** Combina distancia + peso con pesos configurables. */
public class EstrategiaHibrida implements EstrategiaCalculoCosto {
    private final double tarifaKm;
    private final double tarifaKg;

    public EstrategiaHibrida(double tarifaKm, double tarifaKg) {
        this.tarifaKm = tarifaKm;
        this.tarifaKg = tarifaKg;
    }

    public EstrategiaHibrida() { this(1.0, 3.0); }

    @Override
    public double calcular(Envio envio) {
        return envio.getPeso() * 500.0 * tarifaKm + envio.getPeso() * tarifaKg;
    }
}
