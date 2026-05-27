package com.logismart.infraestructura.comportamiento.template;

import com.logismart.dominio.Envio;

public class ProcesoNacional extends ProcesoEnvio {
    @Override
    protected void validar(Envio envio) {
        System.out.println("Validando envio nacional: " + envio.getId());
    }

    @Override
    protected void calcularCosto(Envio envio) {
        envio.setCosto(envio.getPeso() * 100.0);
        System.out.println("Costo nacional calculado: " + envio.getCosto());
    }

    @Override
    protected void procesarPago(Envio envio) {
        System.out.println("Pago nacional procesado para: " + envio.getId());
    }

    @Override
    protected void notificar(Envio envio) {
        System.out.println("Notificacion nacional enviada para: " + envio.getId());
    }
}
