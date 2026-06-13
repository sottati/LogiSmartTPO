package com.logismart.dominio.centro;

import com.logismart.dominio.envio.Envio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Hoja Composite: punto físico de distribución con capacidad fija. */
public class SucursalEntrega extends CentroDistribucion {

    private final int               capacidad;
    private final double            costoOperativo;
    private final List<Envio>       enviosAlmacenados = new ArrayList<>();

    public SucursalEntrega(String nombre, String ubicacion, String codigo,
                           int capacidad, double costoOperativo) {
        super(nombre, ubicacion, codigo);
        this.capacidad      = capacidad;
        this.costoOperativo = costoOperativo;
    }

    public SucursalEntrega(String nombre, String ubicacion, String codigo, int capacidad) {
        this(nombre, ubicacion, codigo, capacidad, 0.0);
    }

    public void agregarEnvio(Envio envio) {
        if (enviosAlmacenados.size() >= capacidad)
            throw new IllegalStateException("Capacidad de sucursal excedida: " + nombre);
        enviosAlmacenados.add(envio);
    }

    public void removerEnvio(Envio envio) { enviosAlmacenados.remove(envio); }

    public List<Envio> getEnviosAlmacenados() {
        return Collections.unmodifiableList(enviosAlmacenados);
    }

    @Override public int    obtenerCapacidad()      { return capacidad; }
    @Override public int    obtenerOcupacion()      { return enviosAlmacenados.size(); }
    public    double        getCostoOperativo()     { return costoOperativo; }

    @Override
    public void mostrar(int prof) {
        System.out.println(" ".repeat(prof) + "[Sucursal] " + nombre
            + " (" + enviosAlmacenados.size() + "/" + capacidad + ")");
    }
}
