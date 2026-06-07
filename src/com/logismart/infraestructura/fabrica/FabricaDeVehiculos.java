package com.logismart.infraestructura.fabrica;

import com.logismart.dominio.vehiculo.Auto;
import com.logismart.dominio.vehiculo.Camion;
import com.logismart.dominio.vehiculo.Moto;
import com.logismart.dominio.vehiculo.Vehiculo;

import java.util.UUID;

/**
 * Factory Method para centralizar creacion de vehiculos por tipo.
 * Retorna la subclase concreta para que getCostoBaseKm() sea polimorfico.
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
        String id = UUID.randomUUID().toString();
        String patenteFinal = (patente == null || patente.isBlank())
                ? "PENDIENTE-" + id.substring(0, 8)
                : patente;

        switch (tipo) {
            case CAMION:  return new Camion(id, patenteFinal);
            case MOTO:    return new Moto(id, patenteFinal);
            case AUTO:    return new Auto(id, patenteFinal);
            default: throw new IllegalArgumentException("Tipo de vehiculo no soportado: " + tipo);
        }
    }
}

