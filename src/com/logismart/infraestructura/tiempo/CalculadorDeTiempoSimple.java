package com.logismart.infraestructura.tiempo;

import com.logismart.dominio.Ruta;

/**
 * GRASP Protected Variations: Calcula el tiempo de entrega solo en base a distancia y velocidad promedio.
 * Modelo simplificado sin considerar trafico ni franjas horarias.
 */
public class CalculadorDeTiempoSimple implements CalculadorDeTiempo {

    private static final double VELOCIDAD_PROMEDIO_KMH = 50.0;
    private static final int TIEMPO_PARADA_MIN = 10;

    @Override
    public int estimarMinutos(Ruta ruta) {
        double distanciaKm = ruta.calcularDistanciaTotal();
        int tiempoTransito = (int) ((distanciaKm / VELOCIDAD_PROMEDIO_KMH) * 60);
        int tiempoParadas = ruta.getParadas().size() * TIEMPO_PARADA_MIN;
        return tiempoTransito + tiempoParadas;
    }

    @Override
    public String getModelo() {
        return String.format("Modelo simple: %.0f km/h promedio + %d min/parada",
                VELOCIDAD_PROMEDIO_KMH, TIEMPO_PARADA_MIN);
    }
}
