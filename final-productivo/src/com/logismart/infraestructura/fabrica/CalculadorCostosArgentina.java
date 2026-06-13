package com.logismart.infraestructura.fabrica;

public class CalculadorCostosArgentina implements CalculadorCostos {
    private static final double IVA = 1.21;

    @Override
    public double calcular(double distanciaKm, double pesoKg) {
        return (distanciaKm * 1.0 + pesoKg * 5.0) * IVA;
    }

    @Override
    public String obtenerNombreRegion() { return "Argentina (IVA 21%)"; }
}
