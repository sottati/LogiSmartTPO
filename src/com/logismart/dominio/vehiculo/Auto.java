package com.logismart.dominio.vehiculo;

import java.util.UUID;

/** Subclase de Vehiculo usada por LogiSmartFactoryArgentina (Abstract Factory). */
public class Auto extends Vehiculo {

    public Auto() {
        super(UUID.randomUUID().toString(), "N/A", 500.0, "AUTO", true);
    }

    public Auto(String id, String patente) {
        super(id, patente, 500.0, "AUTO", true);
    }

    @Override
    public double getCostoBaseKm() {
        return 1.0;
    }

    @Override
    public void conducir() {
        System.out.println("[Auto] Conduciendo automóvil por la ciudad.");
    }
}

