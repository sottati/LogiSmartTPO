package com.logismart.infraestructura.estructural.composite.centro;

import com.logismart.dominio.envio.Envio;
import java.util.ArrayList;
import java.util.List;

public class SucursalEntrega extends CentroDistribucionComposite {
    private final int capacidad;
    private final List<Envio> enviosAlmacenados = new ArrayList<>();

    public SucursalEntrega(String nombre, String ubicacion, String codigo, int capacidad) {
        super(nombre, ubicacion, codigo);
        this.capacidad = capacidad;
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
    public int obtenerCapacidad() {
        return capacidad;
    }

    @Override
    public int obtenerOcupacion() {
        return enviosAlmacenados.size();
    }

    @Override
    public void mostrar(int profundidad) {
        System.out.println(" ".repeat(profundidad) + nombre + " (" + obtenerOcupacion() + "/" + capacidad + ")");
    }
}
