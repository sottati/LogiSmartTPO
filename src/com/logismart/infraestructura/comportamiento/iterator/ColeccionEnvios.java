package com.logismart.infraestructura.comportamiento.iterator;

import com.logismart.dominio.envio.Envio;

/**
 * Interfaz Colección - todo repositorio de Envíos capaz de proveer
 * un iterador debe implementar este contrato.
 *
 * Desacopla al cliente de la estructura de almacenamiento subyacente:
 * el cliente sólo necesita crearIterador() para recorrer la colección.
 *
 * Patrón: Iterator (GoF) - Hito 11
 */
public interface ColeccionEnvios {

    /** @return un nuevo iterador posicionado al inicio de la colección */
    IteradorEnvios crearIterador();

    void agregar(Envio envio);

    void remover(Envio envio);

    /** @return cantidad actual de elementos */
    int obtenerTamaño();
}

