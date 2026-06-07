package com.logismart.infraestructura.costo;

import com.logismart.dominio.envio.Envio;

/**
 * GRASP Protected Variations: Abstrae el algoritmo de calculo de costo de envio.
 * Punto de variacion: la tarifa puede ser fija, por peso, por distancia o combinada.
 * El resto del sistema (Controller, Envio) no necesita saber como se calcula.
 */
public interface CalculadorDeCosto {

    /**
     * Calcula el costo total del envio segun la politica de esta implementacion.
     *
     * @param envio el envio a presupuestar
     * @return costo en pesos (ARS)
     */
    double calcular(Envio envio);

    /**
     * Descripcion de la politica de costo aplicada.
     */
    String getDescripcion();
}

