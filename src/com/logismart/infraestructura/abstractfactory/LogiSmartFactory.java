package com.logismart.infraestructura.abstractfactory;

import com.logismart.dominio.Vehiculo;

/**
 * Abstract Factory - define la familia de objetos que varía por región.
 *
 * Patrón Abstract Factory: permite crear familias completas de objetos
 * relacionados sin especificar sus clases concretas. Cambiar de región
 * solo requiere cambiar la factory concreta.
 */
public interface LogiSmartFactory {
    Vehiculo crearVehiculo();
    CalculadorCostos crearCalculadorCostos();
    ProveedorMapas crearProveedorMapas();
}
