package com.logismart.infraestructura.costo;

import com.logismart.dominio.envio.Envio;

/**
 * GRASP Protected Variations: Politica de costo con tarifa fija independiente del peso/distancia.
 */
public class CalculadorDeCostoFijo implements CalculadorDeCosto {

    private final double tarifaFija;

    public CalculadorDeCostoFijo(double tarifaFija) {
        if (tarifaFija < 0) {
            throw new IllegalArgumentException("La tarifa fija no puede ser negativa");
        }
        this.tarifaFija = tarifaFija;
    }

    @Override
    public double calcular(Envio envio) {
        return tarifaFija;
    }

    @Override
    public String getDescripcion() {
        return String.format("Tarifa fija: $%.2f por envio", tarifaFija);
    }
}

