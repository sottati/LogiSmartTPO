package com.logismart.infraestructura.tiempo;

import com.logismart.dominio.ruta.Ruta;

import java.time.LocalTime;

/**
 * GRASP Protected Variations: Calcula el tiempo de entrega considerando franjas de trafico.
 * Aplica un factor de congestion segun la hora del dia para rutas urbanas.
 * En produccion podria consultar una API de trafico en tiempo real.
 */
public class CalculadorDeTiempoConTrafico implements CalculadorDeTiempo {

    private static final double VELOCIDAD_BASE_KMH = 60.0;
    private static final int TIEMPO_PARADA_MIN = 12;

    @Override
    public int estimarMinutos(Ruta ruta) {
        double factorTrafico = factorDeCongestion(LocalTime.now());
        double distanciaKm = ruta.calcularDistanciaTotal();
        double velocidadEfectiva = VELOCIDAD_BASE_KMH / factorTrafico;
        int tiempoTransito = (int) ((distanciaKm / velocidadEfectiva) * 60);
        int tiempoParadas = ruta.getParadas().size() * TIEMPO_PARADA_MIN;
        return tiempoTransito + tiempoParadas;
    }

    /**
     * Factor de congestion segun franja horaria.
     * 1.0 = sin congestion, 2.0 = doble del tiempo normal.
     */
    private double factorDeCongestion(LocalTime hora) {
        int h = hora.getHour();
        if ((h >= 7 && h < 10) || (h >= 17 && h < 20)) {
            return 2.0; // hora pico
        } else if ((h >= 10 && h < 17) || (h >= 20 && h < 23)) {
            return 1.3; // trafico moderado
        }
        return 1.0; // madrugada / noche
    }

    @Override
    public String getModelo() {
        return "Modelo con trafico: velocidad ajustada segun franja horaria";
    }
}

