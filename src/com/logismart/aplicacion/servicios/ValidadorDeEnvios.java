package com.logismart.aplicacion.servicios;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.ruta.Ruta;
import com.logismart.dominio.vehiculo.Vehiculo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * GRASP Pure Fabrication: Clase artificial que centraliza la validacion de envios.
 * Las reglas de validacion no pertenecen a Envio (no es su responsabilidad validarse a si mismo
 * en el contexto del sistema completo) ni al Controller (que delegaria toda su logica).
 * Se crea esta clase artificial para mantener alta cohesion en ambas clases.
 */
public class ValidadorDeEnvios {

    /**
     * Resultado inmutable de una validacion con lista de errores encontrados.
     */
    public record ResultadoValidacion(boolean esValido, List<String> errores) {
        public ResultadoValidacion {
            errores = List.copyOf(errores);
        }
    }

    /**
     * Valida los datos basicos para crear un envio.
     */
    public ResultadoValidacion validarCreacion(String idCliente, String prioridad, LocalDateTime fechaProgramada) {
        List<String> errores = new ArrayList<>();

        if (idCliente == null || idCliente.isBlank()) {
            errores.add("El ID del cliente es obligatorio");
        }
        if (prioridad == null || prioridad.isBlank()) {
            errores.add("La prioridad es obligatoria");
        } else if (!List.of("ALTA", "MEDIA", "BAJA").contains(prioridad.toUpperCase())) {
            errores.add("Prioridad invalida. Debe ser ALTA, MEDIA o BAJA");
        }
        if (fechaProgramada == null) {
            errores.add("La fecha programada es obligatoria");
        } else if (fechaProgramada.isBefore(LocalDateTime.now())) {
            errores.add("La fecha programada no puede estar en el pasado");
        }

        return new ResultadoValidacion(errores.isEmpty(), errores);
    }

    /**
     * Valida que un envio pueda ser asignado a una ruta con un vehiculo dado.
     */
    public ResultadoValidacion validarAsignacionRuta(Envio envio, Ruta ruta, Vehiculo vehiculo) {
        List<String> errores = new ArrayList<>();

        if (envio == null) {
            errores.add("El envio no puede ser nulo");
            return new ResultadoValidacion(false, errores);
        }
        if (ruta == null) {
            errores.add("La ruta no puede ser nula");
        }
        if (vehiculo == null) {
            errores.add("El vehiculo no puede ser nulo");
        } else if (!vehiculo.estaOperativo()) {
            errores.add("El vehiculo " + vehiculo.getId() + " no esta disponible");
        }
        if (!"PENDIENTE".equals(envio.getEstado())) {
            errores.add("Solo se puede asignar ruta a envios en estado PENDIENTE");
        }
        if (envio.getOrdenes().isEmpty()) {
            errores.add("El envio no tiene ordenes asociadas");
        }

        return new ResultadoValidacion(errores.isEmpty(), errores);
    }
}
