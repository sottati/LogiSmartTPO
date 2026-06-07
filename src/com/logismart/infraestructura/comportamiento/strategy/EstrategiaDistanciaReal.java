package com.logismart.infraestructura.comportamiento.strategy;

import com.logismart.dominio.envio.Envio;

public class EstrategiaDistanciaReal implements EstrategiaCalculoCosto {

    private final double costoPorKm;

    public EstrategiaDistanciaReal(double costoPorKm) {
        if (costoPorKm <= 0) throw new IllegalArgumentException("El costo por km debe ser positivo");
        this.costoPorKm = costoPorKm;
    }

    @Override
    public double calcular(Envio envio) {
        if (envio.getSeguimiento() == null) return 0.0;
        return costoPorKm * 10.0; // placeholder: distancia real via calcularDistanciaTotal()
    }

    @Override
    public String obtenerNombre() {
        return String.format("Tarifa por distancia: $%.2f / km", costoPorKm);
    }
}
