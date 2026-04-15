package com.logismart.infraestructura.ruta;

import com.logismart.dominio.Envio;
import com.logismart.dominio.Ruta;

import java.util.List;

/**
 * GRASP Polymorphism: Estrategia para seleccionar la ruta optima entre un conjunto disponible.
 * Reemplaza condicionales if/else sobre el tipo de algoritmo de asignacion de ruta.
 * Implementaciones: RutaMasCercana, RutaMenosCongestionada, RutaMasBarata.
 */
public interface CalculadorDeRuta {

    /**
     * Selecciona la mejor ruta para el envio dado, segun el criterio de esta estrategia.
     *
     * @param envio   envio que necesita una ruta
     * @param rutas   lista de rutas candidatas disponibles
     * @return la ruta elegida segun el criterio de esta estrategia
     */
    Ruta calcular(Envio envio, List<Ruta> rutas);

    /**
     * Nombre descriptivo del criterio de seleccion.
     */
    String getDescripcion();
}
