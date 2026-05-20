package com.logismart.infraestructura.fabrica;

import com.logismart.dominio.Auto;
import com.logismart.dominio.Camion;
import com.logismart.dominio.Moto;
import com.logismart.dominio.Vehiculo;

/**
 * Factory Method para vehículos - API basada en String.
 * Complementa FabricaDeVehiculos (que recibe TipoVehiculo enum) para contextos
 * donde el tipo llega como texto (p.ej. desde configuración o desde una factory regional).
 */
public final class VehiculoFactory {

    private VehiculoFactory() {}

    public static Vehiculo crearVehiculo(String tipo) {
        switch (tipo.toLowerCase()) {
            case "auto":   return new Auto();
            case "moto":   return new Moto();
            case "camion": return new Camion();
            default:
                throw new IllegalArgumentException("Tipo de vehículo desconocido: " + tipo);
        }
    }
}
