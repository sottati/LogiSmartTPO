package com.logismart.infraestructura.comportamiento.mediator;

import com.logismart.dominio.Envio;

/**
 * Mediator concreto - orquesta el pipeline completo de un envío.
 *
 * Flujo de eventos:
 *   ENVIO_CREADO → auditoria + validador
 *   VALIDACION_OK → auditoria + pago
 *   PAGO_CONFIRMADO → auditoria + notificador
 *   NOTIFICACION_ENVIADA → auditoria + centro.registrarEnvio
 *   ENVIO_REGISTRADO → auditoria (fin del pipeline)
 *   VALIDACION_FALLIDA → auditoria (envío rechazado)
 *
 * Ningún componente conoce a los demás: sólo conocen al mediador.
 *
 * Patrón: Mediator (GoF) - Hito 11
 */
public class MediadorEnviosConcreto implements MediadorEnvios {

    private CentroDistribucion  centro;
    private ValidadorEnvio      validador;
    private SistemaPago         pago;
    private SistemaNotificacion notificador;
    private SistemaAuditoria    auditoria;

    @Override public void registrarCentro(CentroDistribucion c)       { this.centro       = c; }
    @Override public void registrarValidador(ValidadorEnvio v)        { this.validador    = v; }
    @Override public void registrarPago(SistemaPago p)                { this.pago         = p; }
    @Override public void registrarNotificador(SistemaNotificacion n) { this.notificador  = n; }
    @Override public void registrarAuditoria(SistemaAuditoria a)      { this.auditoria    = a; }

    @Override
    public void notificar(String evento, Object datos) {
        switch (evento) {

            case "ENVIO_CREADO":
                Envio envioCreado = (Envio) datos;
                System.out.println("[Mediator] Evento: ENVIO_CREADO - " + envioCreado.getId());
                auditoria.registrar("ENVIO_CREADO", envioCreado);
                validador.validar(envioCreado);
                break;

            case "VALIDACION_OK":
                System.out.println("[Mediator] Evento: VALIDACION_OK");
                auditoria.registrar("VALIDACION_OK", datos);
                pago.procesarPago((Envio) datos);
                break;

            case "VALIDACION_FALLIDA":
                System.out.println("[Mediator] Evento: VALIDACION_FALLIDA - envío rechazado");
                auditoria.registrar("VALIDACION_FALLIDA", datos);
                break;

            case "PAGO_CONFIRMADO":
                System.out.println("[Mediator] Evento: PAGO_CONFIRMADO");
                auditoria.registrar("PAGO_CONFIRMADO", datos);
                notificador.enviarConfirmacion((Envio) datos);
                break;

            case "NOTIFICACION_ENVIADA":
                System.out.println("[Mediator] Evento: NOTIFICACION_ENVIADA");
                auditoria.registrar("NOTIFICACION_ENVIADA", datos);
                centro.registrarEnvio((Envio) datos);
                break;

            case "ENVIO_REGISTRADO":
                System.out.println("[Mediator] Evento: ENVIO_REGISTRADO - pipeline completo");
                auditoria.registrar("ENVIO_REGISTRADO", datos);
                break;

            default:
                System.out.println("[Mediator] Evento desconocido: " + evento);
        }
    }
}
