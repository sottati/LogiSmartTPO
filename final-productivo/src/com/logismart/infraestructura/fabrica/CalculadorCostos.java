package com.logismart.infraestructura.fabrica;

public interface CalculadorCostos {
    double calcular(double distanciaKm, double pesoKg);
    String obtenerNombreRegion();
}
