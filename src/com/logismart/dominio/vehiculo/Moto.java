package com.logismart.dominio.vehiculo;

import java.util.UUID;

/** Subclase de Vehiculo usada por LogiSmartFactoryBrasil (Abstract Factory). */
public class Moto extends Vehiculo {

    public Moto() {
        super(UUID.randomUUID().toString(), "N/A", 100.0, "MOTO", true);
    }

    @Override
    public void conducir() {
        System.out.println("[Moto] Conduciendo moto por las calles.");
    }
}

