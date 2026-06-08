package com.logismart.infraestructura.comportamiento.mediator;

import com.logismart.dominio.envio.Envio;

/**
 * Componente Mediator - Sistema de Notificación al cliente.
 * Envía la confirmación de pago al destinatario del envío.
 *
 * Patrón: Mediator (GoF) - Hito 11
 */
public class SistemaNotificacion {

    private final MediadorEnvios mediador;

    public SistemaNotificacion(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void enviarConfirmacion(Envio envio) {
        System.out.println("[Notificacion] Enviando confirmación para: " + envio.getId()
                + " → " + envio.getDestino());
        System.out.println("[Notificacion] ✓ Confirmación enviada");
        mediador.notificar("NOTIFICACION_ENVIADA", envio);
    }
}

