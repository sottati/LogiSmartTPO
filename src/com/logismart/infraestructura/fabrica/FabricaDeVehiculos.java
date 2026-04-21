package com.logismart.infraestructura.fabrica;

import com.logismart.dominio.Vehiculo;

import java.util.UUID;

/**
 * Factory Method para centralizar creacion de vehiculos por tipo.
 */
public final class FabricaDeVehiculos {

    private FabricaDeVehiculos() {
    }

    public static Vehiculo crearVehiculo(TipoVehiculo tipo) {
        return crearVehiculo(tipo, "PENDIENTE");
    }

    public static Vehiculo crearVehiculo(TipoVehiculo tipo, String patente) {
        if (tipo == null) {
            throw new IllegalArgumentException("Tipo de vehiculo invalido");
        }
        String patenteFinal = (patente == null || patente.isBlank())
                ? "PENDIENTE-" + UUID.randomUUID().toString().substring(0, 8)
                : patente;

        return new Vehiculo(
                UUID.randomUUID().toString(),
                patenteFinal,
                tipo.getCapacidadKgBase(),
                tipo.name(),
                true
        );
    }
}
