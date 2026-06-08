package com.logismart.infraestructura.estructural.flyweight.ubicacion;

import java.util.Objects;

public class Ubicacion {
    private final String ciudad;
    private final String provincia;
    private final String codigoPostal;

    public Ubicacion(String ciudad, String provincia, String codigoPostal) {
        this.ciudad = ciudad;
        this.provincia = provincia;
        this.codigoPostal = codigoPostal;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (objeto == null || getClass() != objeto.getClass()) {
            return false;
        }
        Ubicacion ubicacion = (Ubicacion) objeto;
        return ciudad.equals(ubicacion.ciudad)
                && provincia.equals(ubicacion.provincia)
                && codigoPostal.equals(ubicacion.codigoPostal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ciudad, provincia, codigoPostal);
    }

    @Override
    public String toString() {
        return ciudad + ", " + provincia + " (" + codigoPostal + ")";
    }
}
