package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.envio.Envio;

public class EstadoPendiente implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        envio.cambiarEstado(new EstadoConfirmado());
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("No se puede entregar un envio pendiente");
    }

    @Override
    public void cancelar(Envio envio) {
        envio.cambiarEstado(new EstadoCancelado());
    }

    @Override
    public void retener(Envio envio) {
        envio.cambiarEstado(new EstadoRetenido());
    }

    @Override
    public void devolver(Envio envio) {
        System.out.println("No se puede devolver un envio pendiente");
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("No se puede reclamar un envio pendiente");
    }

    @Override
    public String obtenerNombre() {
        return "PENDIENTE";
    }
}
