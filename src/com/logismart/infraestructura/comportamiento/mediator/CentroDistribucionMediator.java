package com.logismart.infraestructura.comportamiento.mediator;

import com.logismart.dominio.envio.Envio;

/**
 * Componente Mediator - Centro de Distribución.
 * Inicia el pipeline publicando "ENVIO_CREADO" y registra el envío al final.
 * No conoce a ValidadorEnvioMediator, SistemaPago ni SistemaNotificacion.
 *
 * Patrón: Mediator (GoF) - Hito 11
 */
public class CentroDistribucionMediator {

    private final MediadorEnvios mediador;

    public CentroDistribucionMediator(MediadorEnvios mediador) {
        this.mediador = mediador;
    }

    public void crearEnvio(Envio envio) {
        System.out.println("[Centro] Iniciando pipeline para envío: " + envio.getId());
        mediador.notificar("ENVIO_CREADO", envio);
    }

    public void registrarEnvio(Envio envio) {
        System.out.println("[Centro] Registrando envío en el sistema: " + envio.getId());
        mediador.notificar("ENVIO_REGISTRADO", envio);
    }
}
