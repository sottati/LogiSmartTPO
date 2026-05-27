package com.logismart.infraestructura.comportamiento.visitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NodoCentroRegional implements ElementoDistribucion {
    private final String nombre;
    private final List<ElementoDistribucion> hijos = new ArrayList<>();

    public NodoCentroRegional(String nombre) {
        this.nombre = nombre;
    }

    public void agregar(ElementoDistribucion elemento) {
        hijos.add(elemento);
    }

    @Override
    public void aceptar(VisitorCentro visitor) {
        visitor.visitar(this);
        for (ElementoDistribucion hijo : hijos) {
            hijo.aceptar(visitor);
        }
    }

    @Override
    public String obtenerNombre() {
        return nombre;
    }

    public List<ElementoDistribucion> obtenerHijos() {
        return Collections.unmodifiableList(hijos);
    }
}
