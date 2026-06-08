package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.envio.Envio;

public class EstadoRetenido implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        envio.cambiarEstado(new EstadoEnTransito());
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("No se puede entregar un envio retenido");
    }

    @Override
    public void cancelar(Envio envio) {
        envio.cambiarEstado(new EstadoCancelado());
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("El envio ya esta retenido");
    }

    @Override
    public void devolver(Envio envio) {
        envio.cambiarEstado(new EstadoConfirmado());
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("Reclamo registrado para envio retenido");
    }

    @Override
    public String obtenerNombre() {
        return "RETENIDO";
    }
}

