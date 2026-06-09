package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.envio.Envio;

public class EstadoEnCurso implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("El envio ya fue validado y esta en curso");
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
        System.out.println("No se puede devolver un envio en curso");
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("Reclamo registrado para envio en curso");
    }

    @Override
    public String obtenerNombre() {
        return "EN_CURSO";
    }
}
