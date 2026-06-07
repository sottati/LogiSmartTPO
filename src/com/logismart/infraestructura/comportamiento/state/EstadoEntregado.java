package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.envio.Envio;

public class EstadoEntregado implements EstadoEnvio {
    @Override
    public void validar(Envio envio) {
        System.out.println("No se puede validar un envio entregado");
    }

    @Override
    public void entregar(Envio envio) {
        System.out.println("El envio ya fue entregado");
    }

    @Override
    public void cancelar(Envio envio) {
        System.out.println("No se puede cancelar un envio entregado");
    }

    @Override
    public void retener(Envio envio) {
        System.out.println("No se puede retener un envio entregado");
    }

    @Override
    public void devolver(Envio envio) {
        envio.cambiarEstado(new EstadoConfirmado());
    }

    @Override
    public void reclamar(Envio envio) {
        System.out.println("Reclamo registrado para envio entregado");
    }

    @Override
    public String obtenerNombre() {
        return "ENTREGADO";
    }
}

