package com.logismart.infraestructura.estructural.tiempo;

import com.logismart.dominio.ruta.Ruta;

/**
 * GRASP Protected Variations: Abstrae el calculo de tiempo estimado de entrega.
 * Punto de variacion: puede basarse solo en distancia, o considerar trafico y horario.
 * El Controller siempre recibe minutos estimados sin saber como se calculan.
 */
public interface CalculadorDeTiempo {

    /**
     * Estima el tiempo de entrega en minutos para la ruta dada.
     *
     * @param ruta ruta con paradas y vehiculo asignado
     * @return tiempo estimado de entrega en minutos
     */
    int estimarMinutos(Ruta ruta);

    /**
     * Descripcion del modelo de calculo utilizado.
     */
    String getModelo();
}

