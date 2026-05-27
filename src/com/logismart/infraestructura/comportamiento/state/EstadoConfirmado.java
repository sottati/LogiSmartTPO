package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.Envio;

public class EstadoConfirmado implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        envio.cambiarEstado(new EstadoEnTransito());
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("No se puede entregar un envio confirmado");
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
        System.out.println("No se puede devolver un envio confirmado");
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("No se puede reclamar un envio confirmado");
    }

    @Override
    public String obtenerNombre() {
        return "CONFIRMADO";
    }
}
