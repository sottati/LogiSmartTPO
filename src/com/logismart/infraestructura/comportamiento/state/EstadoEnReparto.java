package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.envio.Envio;

public class EstadoEnReparto implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("El envio ya fue validado");
    }

    @Override
    public void entregar(Envio envio) {
        envio.cambiarEstado(new EstadoEntregado());
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("No se puede cancelar un envio en reparto");
    }

    @Override
    public void retener(Envio envio) {
        envio.cambiarEstado(new EstadoRetenido());
    }

    @Override
    public void devolver(Envio envio) {
        envio.cambiarEstado(new EstadoEnTransito());
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("Reclamo registrado para envio en reparto");
    }

    @Override
    public String obtenerNombre() {
        return "EN_REPARTO";
    }
}

