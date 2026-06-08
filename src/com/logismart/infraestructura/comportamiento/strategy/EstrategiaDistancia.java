package com.logismart.infraestructura.comportamiento.strategy;

import com.logismart.dominio.envio.Envio;

public class EstrategiaDistancia implements EstrategiaCalculoCosto {
    private static final double DISTANCIA_FIJA_KM = 500.0;

    @Override
    public double calcular(Envio envio) {
        return envio.getPeso() * DISTANCIA_FIJA_KM;
    }

    @Override
    public String obtenerNombre() {
        return "DISTANCIA";
    }
}

