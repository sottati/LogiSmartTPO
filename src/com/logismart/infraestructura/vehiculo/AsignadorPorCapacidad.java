package com.logismart.infraestructura.vehiculo;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.vehiculo.Flota;
import com.logismart.dominio.envio.Orden;
import com.logismart.dominio.vehiculo.Vehiculo;

import java.util.Optional;

/**
 * GRASP Protected Variations: Asigna el vehiculo disponible con la menor capacidad
 * que aun pueda transportar el peso total del envio.
 * Optimiza el uso de la flota evitando asignar camiones a cargas pequenas.
 * Reutiliza Vehiculo.puedeCargar() (Expert del Hito 4).
 */
public class AsignadorPorCapacidad implements AsignadorDeVehiculos {

    @Override
    public Optional<Vehiculo> asignar(Flota flota, Envio envio) {
        double pesoTotal = envio.getOrdenes().stream()
                .mapToInt(Orden::getTotalBultos)
                .sum();

        return flota.obtenerDisponibles().stream()
                .filter(v -> v.puedeCargar(pesoTotal))
                .min((v1, v2) -> Double.compare(v1.getCapacidadKg(), v2.getCapacidadKg()));
    }

    @Override
    public String getCriterio() {
        return "Vehiculo disponible con menor capacidad suficiente para la carga";
    }
}

