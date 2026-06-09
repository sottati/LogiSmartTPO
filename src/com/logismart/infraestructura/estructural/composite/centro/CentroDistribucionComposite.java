package com.logismart.infraestructura.estructural.composite.centro;

import com.logismart.infraestructura.comportamiento.visitor.ElementoDistribucion;
import com.logismart.infraestructura.comportamiento.visitor.VisitorCentro;

public abstract class CentroDistribucionComposite implements ElementoDistribucion {
    protected String nombre;
    protected String ubicacion;
    protected String codigo;

    protected CentroDistribucionComposite(String nombre, String ubicacion, String codigo) {
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.codigo = codigo;
    }

    public abstract int obtenerCapacidad();
    public abstract int obtenerOcupacion();
    public abstract void mostrar(int profundidad);

    @Override
    public abstract void aceptar(VisitorCentro visitor);

    @Override
    public String obtenerNombre() { return nombre; }

    public double obtenerPorcentajeOcupacion() {
        int cap = obtenerCapacidad();
        return cap == 0 ? 0 : (double) obtenerOcupacion() / cap * 100;
    }

    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public String getCodigo() { return codigo; }
}
