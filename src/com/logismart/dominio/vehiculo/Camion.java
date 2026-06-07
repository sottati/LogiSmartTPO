package com.logismart.dominio.vehiculo;

import java.util.UUID;

/** Vehículo de carga pesada (5000 kg). Creado por VehiculoFactory y LogiSmartFactoryBrasil. */
public class Camion extends Vehiculo {

    public Camion() {
        super(UUID.randomUUID().toString(), "N/A", 5000.0, "CAMION", true);
    }

    public Camion(String id, String patente) {
        super(id, patente, 5000.0, "CAMION", true);
    }

    @Override
    public double getCostoBaseKm() {
        return 1.8;
    }

    @Override
    public void conducir() {
        System.out.println("[Camion] Conduciendo camión por la ruta.");
    }
}

