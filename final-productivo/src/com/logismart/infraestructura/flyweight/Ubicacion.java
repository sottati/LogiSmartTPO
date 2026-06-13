package com.logismart.infraestructura.flyweight;

import java.util.Objects;

/**
 * Objeto inmutable de ubicación. Compartido por FabricaUbicaciones (Flyweight):
 * miles de envíos a las mismas 3 ciudades comparten estas 3 instancias en lugar
 * de crear objetos duplicados.
 */
public final class Ubicacion {
    private final String ciudad;
    private final String provincia;
    private final String codigoPostal;

    Ubicacion(String ciudad, String provincia, String codigoPostal) {
        this.ciudad       = ciudad;
        this.provincia    = provincia;
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad()       { return ciudad; }
    public String getProvincia()    { return provincia; }
    public String getCodigoPostal() { return codigoPostal; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ubicacion)) return false;
        Ubicacion u = (Ubicacion) o;
        return Objects.equals(ciudad, u.ciudad)
            && Objects.equals(provincia, u.provincia)
            && Objects.equals(codigoPostal, u.codigoPostal);
    }

    @Override public int    hashCode() { return Objects.hash(ciudad, provincia, codigoPostal); }
    @Override public String toString()  { return ciudad + ", " + provincia + " (" + codigoPostal + ")"; }
}
