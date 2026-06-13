package com.logismart.infraestructura.decorator;

public class EnvioBasico implements ComponenteEnvio {
    private final String origen;
    private final String destino;
    private final double peso;

    public EnvioBasico(String origen, String destino, double peso) {
        this.origen  = origen;
        this.destino = destino;
        this.peso    = peso;
    }

    @Override public double obtenerCosto()         { return peso * 10.0; }
    @Override public int    obtenerTiempoEntrega() { return 3; }
    @Override public String obtenerDescripcion()   { return "Básico: " + origen + " → " + destino + " (" + peso + " kg)"; }
    @Override public String obtenerServicios()     { return "Básico"; }
}
