package com.logismart.infraestructura.estructural.vehiculo;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.vehiculo.Flota;
import com.logismart.dominio.vehiculo.Vehiculo;

import java.util.Optional;

/**
 * GRASP Protected Variations: Asigna el primer vehiculo disponible en la flota.
 * Criterio simple: solo verifica disponibilidad, sin considerar capacidad ni tipo.
 */
public class AsignadorPorDisponibilidad implements AsignadorDeVehiculos {

    @Override
    public Optional<Vehiculo> asignar(Flota flota, Envio envio) {
        return flota.obtenerDisponibles().stream().findFirst();
    }

    @Override
    public String getCriterio() {
        return "Primer vehiculo disponible en la flota";
    }
}

