package com.logismart.infraestructura.comportamiento.strategy;

import com.logismart.dominio.envio.Envio;

public class EstrategiaVolumen implements EstrategiaCalculoCosto {
    @Override
    public double calcular(Envio envio) {
        return envio.getPeso() * 2.0 * 2.0;
    }

    @Override
    public String obtenerNombre() {
        return "VOLUMEN";
    }
}

