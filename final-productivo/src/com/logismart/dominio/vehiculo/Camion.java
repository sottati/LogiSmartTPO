package com.logismart.dominio.vehiculo;

import java.util.UUID;

public class Camion extends Vehiculo {

    public Camion() {
        super(UUID.randomUUID().toString(), "N/A", 5000.0, "CAMION", true);
    }

    public Camion(String id, String patente) {
        super(id, patente, 5000.0, "CAMION", true);
    }

    @Override public double getCostoBaseKm() { return 1.8; }
}
