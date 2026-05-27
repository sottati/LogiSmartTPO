package com.logismart.infraestructura.comportamiento.visitor;

public class NodoPuntoEntrega implements ElementoDistribucion {
    private final String nombre;
    private final int paquetes;
    private final int capacidad;
    private final double costoOperativo;

    public NodoPuntoEntrega(String nombre, int paquetes, int capacidad, double costoOperativo) {
        this.nombre = nombre;
        this.paquetes = paquetes;
        this.capacidad = capacidad;
        this.costoOperativo = costoOperativo;
    }

    @Override
    public void aceptar(VisitorCentro visitor) {
        visitor.visitar(this);
    }

    @Override
    public String obtenerNombre() {
        return nombre;
    }

    public int obtenerPaquetes() {
        return paquetes;
    }

    public int obtenerCapacidad() {
        return capacidad;
    }

    public double obtenerCostoOperativo() {
        return costoOperativo;
    }
}
