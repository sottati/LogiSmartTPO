package com.logismart.dominio;

import java.util.UUID;

/** Subclase de Vehiculo usada por LogiSmartFactoryArgentina (Abstract Factory). */
public class Auto extends Vehiculo {

    public Auto() {
        super(UUID.randomUUID().toString(), "N/A", 500.0, "AUTO", true);
    }

    @Override
    public void conducir() {
        System.out.println("[Auto] Conduciendo automóvil por la ciudad.");
    }
}
