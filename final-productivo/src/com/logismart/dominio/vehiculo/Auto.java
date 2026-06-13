package com.logismart.dominio.vehiculo;

import java.util.UUID;

public class Auto extends Vehiculo {

    public Auto() {
        super(UUID.randomUUID().toString(), "N/A", 500.0, "AUTO", true);
    }

    public Auto(String id, String patente) {
        super(id, patente, 500.0, "AUTO", true);
    }

    @Override public double getCostoBaseKm() { return 1.0; }
}
