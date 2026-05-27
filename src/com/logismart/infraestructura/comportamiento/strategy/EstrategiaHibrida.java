package com.logismart.infraestructura.comportamiento.strategy;

import com.logismart.dominio.Envio;

public class EstrategiaHibrida implements EstrategiaCalculoCosto {
    private final EstrategiaCalculoCosto distancia = new EstrategiaDistancia();
    private final EstrategiaCalculoCosto peso = new EstrategiaPeso();
    private final EstrategiaCalculoCosto urgencia = new EstrategiaUrgencia();

    @Override
    public double calcular(Envio envio) {
        return distancia.calcular(envio) * 0.40
                + peso.calcular(envio) * 0.30
                + urgencia.calcular(envio) * 0.30;
    }

    @Override
    public String obtenerNombre() {
        return "HIBRIDA";
    }
}
