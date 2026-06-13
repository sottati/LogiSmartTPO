package com.logismart.dominio.vehiculo;

import java.util.UUID;

public class Moto extends Vehiculo {

    public Moto() {
        super(UUID.randomUUID().toString(), "N/A", 100.0, "MOTO", true);
    }

    public Moto(String id, String patente) {
        super(id, patente, 100.0, "MOTO", true);
    }

    @Override public double getCostoBaseKm() { return 0.9; }
}
