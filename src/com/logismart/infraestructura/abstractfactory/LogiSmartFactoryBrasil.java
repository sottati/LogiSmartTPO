package com.logismart.infraestructura.abstractfactory;

import com.logismart.dominio.Moto;
import com.logismart.dominio.Vehiculo;

/** Concrete Factory Brasil: Moto + ICMS 12% + HERE Maps. */
public class LogiSmartFactoryBrasil implements LogiSmartFactory {

    @Override
    public Vehiculo crearVehiculo() {
        return new Moto();
    }

    @Override
    public CalculadorCostos crearCalculadorCostos() {
        return new CalculadorCostosBrasil();
    }

    @Override
    public ProveedorMapas crearProveedorMapas() {
        return new HereMaps();
    }
}
