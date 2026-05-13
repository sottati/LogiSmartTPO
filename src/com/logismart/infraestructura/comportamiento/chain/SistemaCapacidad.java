package com.logismart.infraestructura.comportamiento.chain;

public class SistemaCapacidad {
    private double capacidadDisponible;

    public SistemaCapacidad(double capacidadMaxima) {
        this.capacidadDisponible = capacidadMaxima;
    }

    public boolean hayEspacioDisponible(double peso) {
        return capacidadDisponible >= peso;
    }

    public void reducirCapacidad(double peso) {
        capacidadDisponible -= peso;
    }

    public double getCapacidadDisponible() {
        return capacidadDisponible;
    }
}
