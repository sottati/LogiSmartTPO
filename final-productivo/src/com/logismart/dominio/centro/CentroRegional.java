package com.logismart.dominio.centro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Nodo Composite: agrega varios centros o sucursales bajo una región. */
public class CentroRegional extends CentroDistribucion {

    private final List<CentroDistribucion> subcentros = new ArrayList<>();

    public CentroRegional(String nombre, String ubicacion, String codigo) {
        super(nombre, ubicacion, codigo);
    }

    public void agregar(CentroDistribucion c)  { subcentros.add(c); }
    public void remover(CentroDistribucion c)  { subcentros.remove(c); }

    public List<CentroDistribucion> obtenerSubcentros() {
        return Collections.unmodifiableList(subcentros);
    }

    @Override
    public int obtenerCapacidad() {
        int total = 0;
        for (CentroDistribucion c : subcentros) total += c.obtenerCapacidad();
        return total;
    }

    @Override
    public int obtenerOcupacion() {
        int total = 0;
        for (CentroDistribucion c : subcentros) total += c.obtenerOcupacion();
        return total;
    }

    @Override
    public void mostrar(int prof) {
        System.out.println(" ".repeat(prof) + "[Regional] " + nombre
            + " (" + obtenerOcupacion() + "/" + obtenerCapacidad() + ")");
        for (CentroDistribucion c : subcentros) c.mostrar(prof + 2);
    }
}
