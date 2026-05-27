package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.Envio;

public class EstadoEnTransito implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("El envio ya esta en transito");
    }

    @Override
    public void entregar(Envio envio) {
        envio.cambiarEstado(new EstadoEnReparto());
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
        envio.cambiarEstado(new EstadoConfirmado());
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("Reclamo registrado para envio en transito");
    }

    @Override
    public String obtenerNombre() {
        return "EN_TRANSITO";
    }
}
