package com.logismart.infraestructura.comportamiento.strategy;

import com.logismart.dominio.envio.Envio;

public class EstrategiaFijo implements EstrategiaCalculoCosto {

    private final double tarifaFija;

    public EstrategiaFijo(double tarifaFija) {
        if (tarifaFija < 0) throw new IllegalArgumentException("La tarifa fija no puede ser negativa");
        this.tarifaFija = tarifaFija;
    }

    @Override
    public double calcular(Envio envio) {
        return tarifaFija;
    }

    @Override
    public String obtenerNombre() {
        return String.format("Tarifa fija: $%.2f por envio", tarifaFija);
    }
}
