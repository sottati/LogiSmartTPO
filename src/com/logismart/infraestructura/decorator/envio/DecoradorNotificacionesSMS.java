package com.logismart.infraestructura.decorator.envio;

public class DecoradorNotificacionesSMS extends DecoradorEnvio {
    public DecoradorNotificacionesSMS(ComponenteEnvio envio) {
        super(envio);
    }

    @Override
    public double obtenerCosto() {
        return envioDecorado.obtenerCosto() + 25.0;
    }

    @Override
    public String obtenerServicios() {
        return envioDecorado.obtenerServicios() + " + Notificaciones SMS";
    }
}
