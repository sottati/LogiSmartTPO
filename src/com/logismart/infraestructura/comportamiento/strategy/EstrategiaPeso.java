package com.logismart.infraestructura.comportamiento.strategy;

import com.logismart.dominio.envio.Envio;

public class EstrategiaPeso implements EstrategiaCalculoCosto {
    @Override
    public double calcular(Envio envio) {
        return envio.getPeso() * 5.0;
    }

    @Override
    public String obtenerNombre() {
        return "PESO";
    }
}

