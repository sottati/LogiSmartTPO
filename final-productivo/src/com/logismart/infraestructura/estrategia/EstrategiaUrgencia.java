package com.logismart.infraestructura.estrategia;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstrategiaCalculoCosto;

public class EstrategiaUrgencia implements EstrategiaCalculoCosto {
    private static final double RECARGO_URGENCIA = 2.5;

    @Override
    public double calcular(Envio envio) {
        return envio.getPeso() * 5.0 * RECARGO_URGENCIA;
    }
}
