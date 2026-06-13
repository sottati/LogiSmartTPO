package com.logismart.dominio.centro;

/**
 * Componente del patrón Composite.
 * Permite tratar un centro regional y sus sucursales de forma uniforme:
 * obtenerCapacidad() y obtenerOcupacion() funcionan recursivamente en nodos,
 * directamente en hojas.
 */
public abstract class CentroDistribucion {

    protected final String nombre;
    protected final String ubicacion;
    protected final String codigo;

    protected CentroDistribucion(String nombre, String ubicacion, String codigo) {
        this.nombre   = nombre;
        this.ubicacion = ubicacion;
        this.codigo   = codigo;
    }

    public abstract int    obtenerCapacidad();
    public abstract int    obtenerOcupacion();
    public abstract void   mostrar(int profundidad);

    public double obtenerPorcentajeOcupacion() {
        int cap = obtenerCapacidad();
        return cap == 0 ? 0 : (double) obtenerOcupacion() / cap * 100.0;
    }

    public String getNombre()    { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public String getCodigo()    { return codigo; }
}
