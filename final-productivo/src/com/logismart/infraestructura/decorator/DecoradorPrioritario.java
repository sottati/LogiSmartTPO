package com.logismart.infraestructura.decorator;

public class DecoradorPrioritario extends DecoradorEnvio {
    public DecoradorPrioritario(ComponenteEnvio envio) { super(envio); }

    @Override public double obtenerCosto()         { return envioDecorado.obtenerCosto() * 1.30; }
    @Override public int    obtenerTiempoEntrega() { return Math.max(1, envioDecorado.obtenerTiempoEntrega() - 1); }
    @Override public String obtenerServicios()     { return envioDecorado.obtenerServicios() + " + Prioritario"; }
}
