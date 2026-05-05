package com.logismart.infraestructura.composite.centro;

import java.util.ArrayList;
import java.util.List;

public class CentroRegional extends CentroDistribucion {
    private final List<CentroDistribucion> subcentros = new ArrayList<>();

    public CentroRegional(String nombre, String ubicacion, String codigo) {
        super(nombre, ubicacion, codigo);
    }

    public void agregar(CentroDistribucion centro) {
        subcentros.add(centro);
    }

    public void remover(CentroDistribucion centro) {
        subcentros.remove(centro);
    }

    public List<CentroDistribucion> obtenerSubcentros() {
        return new ArrayList<>(subcentros);
    }

    @Override
    public int obtenerCapacidad() {
        int total = 0;
        for (CentroDistribucion centro : subcentros) {
            total += centro.obtenerCapacidad();
        }
        return total;
    }

    @Override
    public int obtenerOcupacion() {
        int total = 0;
        for (CentroDistribucion centro : subcentros) {
            total += centro.obtenerOcupacion();
        }
        return total;
    }

    @Override
    public void mostrar(int profundidad) {
        System.out.println(" ".repeat(profundidad) + nombre + " (" + obtenerOcupacion() + "/" + obtenerCapacidad() + ")");
        for (CentroDistribucion centro : subcentros) {
            centro.mostrar(profundidad + 2);
        }
    }
}
