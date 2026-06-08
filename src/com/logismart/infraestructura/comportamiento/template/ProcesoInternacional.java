package com.logismart.infraestructura.comportamiento.template;

import com.logismart.dominio.envio.Envio;

public class ProcesoInternacional extends ProcesoEnvio {
    @Override
    protected void validar(Envio envio) {
        System.out.println("Validando aduana y documentacion internacional: " + envio.getId());
    }

    @Override
    protected void calcularCosto(Envio envio) {
        envio.setCosto(envio.getPeso() * 250.0 + 1000.0);
        System.out.println("Costo internacional calculado: " + envio.getCosto());
    }

    @Override
    protected void procesarPago(Envio envio) {
        System.out.println("Pago internacional procesado para: " + envio.getId());
    }

    @Override
    protected void notificar(Envio envio) {
        System.out.println("Notificacion internacional enviada para: " + envio.getId());
    }
}

