package com.logismart.aplicacion;

import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.ruta.Ruta;
import com.logismart.infraestructura.estructural.notificacion.Notificador;

/**
 * GRASP Pure Fabrication: Clase artificial sin equivalente en el dominio real.
 * Centraliza la logica de notificacion que no encaja ni en Envio, ni en Ruta, ni en Usuario.
 *
 * GRASP Indirection: Se apoya en la interfaz Notificador para permanecer desacoplada
 * de la implementacion concreta (Email, SMS, Push). El canal se puede cambiar
 * sin modificar este servicio (Open/Closed Principle).
 */
public class ServicioDeNotificaciones {

    private final Notificador notificador;

    public ServicioDeNotificaciones(Notificador notificador) {
        if (notificador == null) {
            throw new IllegalArgumentException("El notificador no puede ser nulo");
        }
        this.notificador = notificador;
    }

    /**
     * Notifica al cliente que su envio fue creado exitosamente.
     */
    public void notificarCreacionDeEnvio(Envio envio, ClienteFinal cliente) {
        String asunto = "Envio creado - ID: " + envio.getId();
        String mensaje = String.format(
                "Hola %s, tu envio con ID %s fue registrado con estado %s.",
                cliente.getNombre(), envio.getId(), envio.getEstado());
        notificador.notificar(cliente.getEmail(), asunto, mensaje);
    }

    /**
     * Notifica al cliente que se asigno una ruta a su envio.
     */
    public void notificarAsignacionDeRuta(Envio envio, Ruta ruta, ClienteFinal cliente) {
        String asunto = "Ruta asignada - Envio: " + envio.getId();
        String mensaje = String.format(
                "Hola %s, se asigno la ruta %s a tu envio. Distancia estimada: %.1f km.",
                cliente.getNombre(), ruta.getId(), ruta.calcularDistanciaTotal());
        notificador.notificar(cliente.getEmail(), asunto, mensaje);
    }

    /**
     * Notifica al cliente que su envio fue entregado.
     */
    public void notificarEntregaExitosa(Envio envio, ClienteFinal cliente) {
        String asunto = "Entrega confirmada - Envio: " + envio.getId();
        String mensaje = String.format(
                "Hola %s, tu envio %s fue entregado exitosamente.",
                cliente.getNombre(), envio.getId());
        notificador.notificar(cliente.getEmail(), asunto, mensaje);
    }

    /**
     * Notifica al cliente que su envio fue cancelado.
     */
    public void notificarCancelacion(Envio envio, ClienteFinal cliente) {
        String asunto = "Envio cancelado - ID: " + envio.getId();
        String mensaje = String.format(
                "Hola %s, lamentamos informarte que el envio %s fue cancelado.",
                cliente.getNombre(), envio.getId());
        notificador.notificar(cliente.getEmail(), asunto, mensaje);
    }

    public String getCanalActivo() {
        return notificador.getCanal();
    }
}

