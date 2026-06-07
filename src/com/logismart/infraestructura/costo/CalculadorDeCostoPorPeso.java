package com.logismart.infraestructura.costo;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.Orden;

/**
 * GRASP Protected Variations: Politica de costo proporcional al total de bultos de las ordenes.
 * Modela escenarios donde el flete se negocia por unidad de carga.
 */
public class CalculadorDeCostoPorPeso implements CalculadorDeCosto {

    private final double costoPorBulto;

    public CalculadorDeCostoPorPeso(double costoPorBulto) {
        if (costoPorBulto <= 0) {
            throw new IllegalArgumentException("El costo por bulto debe ser positivo");
        }
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
    public String getDescripcion() {
        return String.format("Tarifa por bulto: $%.2f / bulto", costoPorBulto);
    }
}

