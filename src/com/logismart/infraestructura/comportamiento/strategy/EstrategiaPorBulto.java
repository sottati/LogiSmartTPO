package com.logismart.infraestructura.comportamiento.strategy;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.Orden;

public class EstrategiaPorBulto implements EstrategiaCalculoCosto {

    private final double costoPorBulto;

    public EstrategiaPorBulto(double costoPorBulto) {
        if (costoPorBulto <= 0) throw new IllegalArgumentException("El costo por bulto debe ser positivo");
        this.costoPorBulto = costoPorBulto;
    }

    @Override
    public double calcular(Envio envio) {
        int totalBultos = envio.getOrdenes().stream()
                .mapToInt(Orden::getTotalBultos)
                .sum();
        return totalBultos * costoPorBulto;
    }

    @Override
    public String obtenerNombre() {
        return String.format("Tarifa por bulto: $%.2f / bulto", costoPorBulto);
    }
}
