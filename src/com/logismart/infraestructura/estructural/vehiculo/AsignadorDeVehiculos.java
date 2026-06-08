package com.logismart.infraestructura.estructural.vehiculo;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.vehiculo.Flota;
import com.logismart.dominio.vehiculo.Vehiculo;

import java.util.Optional;

/**
 * GRASP Protected Variations: Abstrae el algoritmo de asignacion de vehiculos.
 * Punto de variacion: el criterio puede ser disponibilidad, capacidad o proximidad.
 * Permite cambiar la politica de asignacion sin modificar el Controller.
 */
public interface AsignadorDeVehiculos {

    /**
     * Selecciona el vehiculo mas adecuado de la flota para el envio dado.
     *
     * @param flota flota de vehiculos disponibles
     * @param envio envio que requiere vehiculo
     * @return el vehiculo seleccionado, o empty si ninguno es apto
     */
    Optional<Vehiculo> asignar(Flota flota, Envio envio);

    /**
     * Descripcion del criterio de asignacion.
     */
    String getCriterio();
}

