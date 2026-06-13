package com.logismart.infraestructura.fabrica;

import com.logismart.dominio.vehiculo.Vehiculo;

/**
 * Abstract Factory — define la familia de objetos que varía por región.
 * Cambiar de región solo requiere cambiar la factory concreta; ningún
 * otro código conoce qué vehículo, calculador o mapa usa cada región.
 */
public interface LogiSmartFactory {
    Vehiculo         crearVehiculo();
    CalculadorCostos crearCalculadorCostos();
    ProveedorMapas   crearProveedorMapas();
}
