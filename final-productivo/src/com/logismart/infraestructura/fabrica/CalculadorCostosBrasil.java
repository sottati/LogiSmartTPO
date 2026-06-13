package com.logismart.infraestructura.fabrica;

public class CalculadorCostosBrasil implements CalculadorCostos {
    private static final double ICMS = 1.12;

    @Override
    public double calcular(double distanciaKm, double pesoKg) {
        return (distanciaKm * 0.9 + pesoKg * 4.5) * ICMS;
    }

    @Override
    public String obtenerNombreRegion() { return "Brasil (ICMS 12%)"; }
}
