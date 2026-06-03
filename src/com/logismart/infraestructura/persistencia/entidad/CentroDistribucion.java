package com.logismart.infraestructura.persistencia.entidad;

/**
 * Entidad de persistencia plana para CentroDistribucion.
 * Representa una fila en la tabla centros_distribucion.
 *
 * Separada deliberadamente del Composite (infraestructura/composite/centro/CentroDistribucion)
 * que es abstracto y calcula capacidad/ocupacion sumando hijos.
 * Esta clase es una proyeccion snapshot generada por CentroAssembler — no se edita
 * directamente. El Composite es la unica fuente de verdad.
 *
 * Decision de diseno (Hito 13): separar dominio de persistencia evita que el
 * modelo de almacenamiento acople a la jerarquia Composite.
 */
public class CentroDistribucion {
    private String id;
    private String nombre;
    private String ubicacion;
    private String codigo;
    private int capacidad;
    private int ocupacion;

    public CentroDistribucion() {}

    public CentroDistribucion(String id, String nombre, String ubicacion,
                               String codigo, int capacidad, int ocupacion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.codigo = codigo;
        this.capacidad = capacidad;
        this.ocupacion = ocupacion;
    }

    public String getId()           { return id; }
    public void setId(String id)    { this.id = id; }

    public String getNombre()              { return nombre; }
    public void setNombre(String nombre)   { this.nombre = nombre; }

    public String getUbicacion()                { return ubicacion; }
    public void setUbicacion(String ubicacion)  { this.ubicacion = ubicacion; }

    public String getCodigo()              { return codigo; }
    public void setCodigo(String codigo)   { this.codigo = codigo; }

    public int getCapacidad()              { return capacidad; }
    public void setCapacidad(int cap)      { this.capacidad = cap; }

    public int getOcupacion()              { return ocupacion; }
    public void setOcupacion(int ocu)      { this.ocupacion = ocu; }

    @Override
    public String toString() {
        return "CentroDistribucion{id='" + id + "', nombre='" + nombre
             + "', ubicacion='" + ubicacion + "', capacidad=" + capacidad
             + ", ocupacion=" + ocupacion + "}";
    }
}
