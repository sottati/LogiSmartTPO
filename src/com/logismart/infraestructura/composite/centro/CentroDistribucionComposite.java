package com.logismart.infraestructura.composite.centro;

public abstract class CentroDistribucionComposite {
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

    public double obtenerPorcentajeOcupacion() {
        int capacidad = obtenerCapacidad();
        return capacidad == 0 ? 0 : (double) obtenerOcupacion() / capacidad * 100;
    }

    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public String getCodigo() { return codigo; }
}
