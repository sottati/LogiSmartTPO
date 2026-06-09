package com.logismart.infraestructura.estructural.composite.centro;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.comportamiento.visitor.VisitorCentro;
import java.util.ArrayList;
import java.util.List;

public class SucursalEntrega extends CentroDistribucionComposite {
    private final int capacidad;
    private final double costoOperativo;
    private final List<Envio> enviosAlmacenados = new ArrayList<>();

    public SucursalEntrega(String nombre, String ubicacion, String codigo, int capacidad, double costoOperativo) {
        super(nombre, ubicacion, codigo);
        this.capacidad = capacidad;
        this.costoOperativo = costoOperativo;
    }

    public SucursalEntrega(String nombre, String ubicacion, String codigo, int capacidad) {
        this(nombre, ubicacion, codigo, capacidad, 0.0);
    }

    public void agregarEnvio(Envio envio) {
        if (enviosAlmacenados.size() >= capacidad) {
            throw new IllegalStateException("Capacidad excedida");
        }
        enviosAlmacenados.add(envio);
    }

    public void removerEnvio(Envio envio) {
        enviosAlmacenados.remove(envio);
    }

    @Override
    public void aceptar(VisitorCentro visitor) {
        visitor.visitar(this);
    }

    @Override
    public int obtenerCapacidad() {
        return capacidad;
    }

    @Override
    public int obtenerOcupacion() {
        return enviosAlmacenados.size();
    }

    public int obtenerPaquetes() {
        return enviosAlmacenados.size();
    }

    public double obtenerCostoOperativo() {
        return costoOperativo;
    }

    @Override
    public void mostrar(int profundidad) {
        System.out.println(" ".repeat(profundidad) + nombre + " (" + obtenerOcupacion() + "/" + capacidad + ")");
    }
}
