package com.logismart.infraestructura.estructural.composite.centro;

import com.logismart.infraestructura.comportamiento.visitor.VisitorCentro;
import java.util.ArrayList;
import java.util.List;

public class CentroRegional extends CentroDistribucionComposite {
    private final List<CentroDistribucionComposite> subcentros = new ArrayList<>();

    public CentroRegional(String nombre, String ubicacion, String codigo) {
        super(nombre, ubicacion, codigo);
    }

    public void agregar(CentroDistribucionComposite centro) {
        subcentros.add(centro);
    }

    public void remover(CentroDistribucionComposite centro) {
        subcentros.remove(centro);
    }

    public List<CentroDistribucionComposite> obtenerSubcentros() {
        return new ArrayList<>(subcentros);
    }

    @Override
    public void aceptar(VisitorCentro visitor) {
        visitor.visitar(this);
        for (CentroDistribucionComposite sub : subcentros) {
            sub.aceptar(visitor);
        }
    }

    @Override
    public int obtenerCapacidad() {
        int total = 0;
        for (CentroDistribucionComposite centro : subcentros) {
            total += centro.obtenerCapacidad();
        }
        return total;
    }

    @Override
    public int obtenerOcupacion() {
        int total = 0;
        for (CentroDistribucionComposite centro : subcentros) {
            total += centro.obtenerOcupacion();
        }
        return total;
    }

    @Override
    public void mostrar(int profundidad) {
        System.out.println(" ".repeat(profundidad) + nombre + " (" + obtenerOcupacion() + "/" + obtenerCapacidad() + ")");
        for (CentroDistribucionComposite centro : subcentros) {
            centro.mostrar(profundidad + 2);
        }
    }
}
