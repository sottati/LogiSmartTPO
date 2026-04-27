package com.logismart.infraestructura.abstractfactory;

import com.logismart.dominio.Auto;
import com.logismart.dominio.Vehiculo;

/** Concrete Factory Argentina: Auto + IVA 21% + Google Maps. */
public class LogiSmartFactoryArgentina implements LogiSmartFactory {

    @Override
    public Vehiculo crearVehiculo() {
        return new Auto();
    }

    @Override
    public CalculadorCostos crearCalculadorCostos() {
        return new CalculadorCostosArgentina();
    }

    @Override
    public ProveedorMapas crearProveedorMapas() {
        return new GoogleMapsArgentina();
    }
}
