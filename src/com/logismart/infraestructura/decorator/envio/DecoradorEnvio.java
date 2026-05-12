package com.logismart.infraestructura.decorator.envio;

public abstract class DecoradorEnvio implements Envio {
    protected final Envio envioDecorado;

    protected DecoradorEnvio(Envio envio) {
        this.envioDecorado = envio;
    }

    @Override
    public double obtenerCosto() {
        return envioDecorado.obtenerCosto();
    }

    @Override
    public int obtenerTiempoEntrega() {
        return envioDecorado.obtenerTiempoEntrega();
    }

    @Override
    public String obtenerDescripcion() {
        return envioDecorado.obtenerDescripcion();
    }

    @Override
    public String obtenerServicios() {
        return envioDecorado.obtenerServicios();
    }
}
