package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.envio.Envio;

public class EstadoCerrado implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("No se puede validar un envio cerrado");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("No se puede entregar un envio cerrado");
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("No se puede cancelar un envio cerrado");
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("No se puede retener un envio cerrado");
    }

    @Override
    public void devolver(Envio envio) {
        System.out.println("No se puede devolver un envio cerrado");
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("No se puede reclamar un envio cerrado");
    }

    @Override
    public String obtenerNombre() {
        return "CERRADO";
    }
}
