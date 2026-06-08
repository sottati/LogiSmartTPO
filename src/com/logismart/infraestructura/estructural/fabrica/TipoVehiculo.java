package com.logismart.infraestructura.estructural.fabrica;

public enum TipoVehiculo {
    AUTO(500.0),
    CAMION(5000.0),
    MOTO(100.0);

    private final double capacidadKgBase;

    TipoVehiculo(double capacidadKgBase) {
        this.capacidadKgBase = capacidadKgBase;
    }

    public double getCapacidadKgBase() {
        return capacidadKgBase;
    }

    public static TipoVehiculo desdeNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Tipo de vehiculo vacio");
        }
        return valueOf(nombre.trim().toUpperCase());
    }
}
