package com.logismart.infraestructura.decorator;

public class DecoradorRastreoGPS extends DecoradorEnvio {
    public DecoradorRastreoGPS(ComponenteEnvio envio) { super(envio); }

    @Override public double obtenerCosto()     { return envioDecorado.obtenerCosto() + 50.0; }
    @Override public String obtenerServicios() { return envioDecorado.obtenerServicios() + " + GPS"; }
}
