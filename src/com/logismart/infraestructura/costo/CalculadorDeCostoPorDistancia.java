package com.logismart.infraestructura.costo;

import com.logismart.dominio.Envio;

/**
 * GRASP Protected Variations: Politica de costo proporcional a la distancia de la ruta.
 * Delega el calculo de distancia en Ruta.calcularDistanciaTotal() (Expert del Hito 4).
 */
public class CalculadorDeCostoPorDistancia implements CalculadorDeCosto {

    private final double costoPorKm;

    public CalculadorDeCostoPorDistancia(double costoPorKm) {
        if (costoPorKm <= 0) {
            throw new IllegalArgumentException("El costo por km debe ser positivo");
        }
        this.costoPorKm = costoPorKm;
    }

    @Override
    public double calcular(Envio envio) {
        if (envio.getSeguimiento() == null) {
            return 0.0;
        }
        // Se obtiene la ruta a traves del Controller o del contexto del envio
        // En este modelo simplificado, la distancia viene del costo estimado de Ruta
        // que ya usa Expert (calcularDistanciaTotal). Se retorna un valor base para demo.
        return costoPorKm * 10.0; // placeholder: en produccion se obtiene la distancia real
    }

    @Override
    public String getDescripcion() {
        return String.format("Tarifa por distancia: $%.2f / km", costoPorKm);
    }
}
