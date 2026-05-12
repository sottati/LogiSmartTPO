package com.logismart.infraestructura.decorator.envio;

public class DecoradorSeguro extends DecoradorEnvio {
    public DecoradorSeguro(Envio envio) {
        super(envio);
    }

    @Override
    public double obtenerCosto() {
        return envioDecorado.obtenerCosto() * 1.15;
    }

    @Override
    public String obtenerServicios() {
        return envioDecorado.obtenerServicios() + " + Seguro";
    }
}
