package com.logismart.infraestructura.comportamiento.iterator;

import com.logismart.dominio.envio.Envio;

/**
 * Interfaz Iterator - contrato de recorrido uniforme sobre cualquier
 * ColeccionEnvios, independientemente de su estructura interna.
 *
 * Patrón: Iterator (GoF) - Hito 11
 */
public interface IteradorEnvios {

    /** @return true si quedan elementos por recorrer */
    boolean tieneSiguiente();

    /** @return el siguiente Envío en el recorrido */
    Envio obtenerSiguiente();

    /** Reinicia el cursor al primer elemento */
    void reiniciar();
}

