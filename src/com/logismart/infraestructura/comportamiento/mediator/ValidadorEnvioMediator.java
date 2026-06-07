package com.logismart.infraestructura.comportamiento.mediator;

import com.logismart.dominio.envio.Envio;

/**
 * Componente Mediator - Validador de Envíos.
 * Verifica que costo y peso sean positivos antes de autorizar el pago.
 *
 * Patrón: Mediator (GoF) - Hito 11
 */
public class ValidadorEnvioMediator {

    private final MediadorEnvios mediador;

    public ValidadorEnvioMediator(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void validar(Envio envio) {
        System.out.println("[Validador] Validando envío: " + envio.getId());
        if (envio.getCosto() > 0 && envio.getPeso() > 0) {
            System.out.println("[Validador] ✓ Válido");
            mediador.notificar("VALIDACION_OK", envio);
        } else {
            System.out.println("[Validador] ✗ Inválido (costo=" + envio.getCosto()
                    + ", peso=" + envio.getPeso() + ")");
            mediador.notificar("VALIDACION_FALLIDA", envio);
        }
    }
}
