package com.logismart.infraestructura.comportamiento.mediator;

import com.logismart.dominio.envio.Envio;

/**
 * Componente Mediator - Sistema de Pago.
 * Procesa el cobro del envío y notifica confirmación al mediador.
 *
 * Patrón: Mediator (GoF) - Hito 11
 */
public class SistemaPago {

    private final MediadorEnvios mediador;

    public SistemaPago(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void procesarPago(Envio envio) {
        System.out.println("[Pago] Procesando $" + envio.getCosto()
                + " para envío: " + envio.getId());
        System.out.println("[Pago] ✓ Pago confirmado");
        mediador.notificar("PAGO_CONFIRMADO", envio);
    }
}

