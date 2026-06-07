package com.logismart.infraestructura.decorator.envio;

public class EnvioBasico implements ComponenteEnvio {
    private final String origen;
    private final String destino;
    private final double peso;
    private final String numeroSeguimiento;

    public EnvioBasico(String origen, String destino, double peso) {
        this.origen = origen;
        this.destino = destino;
        this.peso = peso;
        this.numeroSeguimiento = "ENV-" + System.currentTimeMillis();
    }

    @Override
    public double obtenerCosto() {
        return peso * 10.0;
    }

    @Override
    public int obtenerTiempoEntrega() {
        return 3;
    }

    @Override
    public String obtenerDescripcion() {
        return "Envio basico: " + origen + " -> " + destino + " (" + peso + " kg, " + numeroSeguimiento + ")";
    }

    @Override
    public String obtenerServicios() {
        return "Basico";
    }
}
