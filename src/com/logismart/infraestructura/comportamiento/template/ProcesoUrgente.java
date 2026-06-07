package com.logismart.infraestructura.comportamiento.template;

import com.logismart.dominio.envio.Envio;

public class ProcesoUrgente extends ProcesoEnvio {
    @Override
    protected void validar(Envio envio) {
        System.out.println("Validando prioridad urgente: " + envio.getId());
    }

    @Override
    protected void calcularCosto(Envio envio) {
        envio.setCosto(envio.getPeso() * 180.0 + 500.0);
        System.out.println("Costo urgente calculado: " + envio.getCosto());
    }

    @Override
    protected void procesarPago(Envio envio) {
        System.out.println("Pago urgente procesado para: " + envio.getId());
    }

    @Override
    protected void notificar(Envio envio) {
        System.out.println("Notificacion urgente enviada para: " + envio.getId());
    }
}

