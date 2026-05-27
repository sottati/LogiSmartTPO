package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.Envio;

public class EstadoCancelado implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("No se puede validar un envio cancelado");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("No se puede entregar un envio cancelado");
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("El envio ya esta cancelado");
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("No se puede retener un envio cancelado");
    }

    @Override
    public void devolver(Envio envio) {
        System.out.println("No se puede devolver un envio cancelado");
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("No se puede reclamar un envio cancelado");
    }

    @Override
    public String obtenerNombre() {
        return "CANCELADO";
    }
}
