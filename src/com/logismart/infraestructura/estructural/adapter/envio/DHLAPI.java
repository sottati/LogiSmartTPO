package com.logismart.infraestructura.estructural.adapter.envio;

public class DHLAPI {
    public String registrarPaquete(String origen, String destino, double peso) {
        return "DHL-" + System.currentTimeMillis();
    }

    public String consultarEstadoPaquete(String codigo) {
        return "En transito";
    }

    public double calcularTarifa(String origen, String destino, double peso) {
        return peso * 15.0;
    }
}
