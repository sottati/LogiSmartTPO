package com.logismart.infraestructura.comportamiento.iterator;

import com.logismart.dominio.envio.Envio;

/**
 * Colección basada en lista enlazada simple.
 * Uso típico: historial cronológico de envíos con inserción O(1) al final.
 *
 * La clase Nodo es privada: ningún cliente conoce la estructura interna.
 * El iterador (IteradorLista) navega de nodo en nodo sin exponer referencias.
 *
 * Patrón: Iterator (GoF) - Hito 11
 */
public class ColeccionLista implements ColeccionEnvios {

    private Nodo cabeza;
    private int  tamaño = 0;

    @Override
    public IteradorEnvios crearIterador() {
        return new IteradorLista();
    }

    @Override
    public void agregar(Envio envio) {
        Nodo nuevo = new Nodo(envio);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo actual = cabeza;
            while (actual.siguiente != null) actual = actual.siguiente;
            actual.siguiente = nuevo;
        }
        tamaño++;
    }

    @Override
    public void remover(Envio envio) {
        if (cabeza == null) return;
        if (cabeza.envio.getId().equals(envio.getId())) {
            cabeza = cabeza.siguiente;
            tamaño--;
            return;
        }
        Nodo actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.envio.getId().equals(envio.getId())) {
                actual.siguiente = actual.siguiente.siguiente;
                tamaño--;
                return;
            }
            actual = actual.siguiente;
        }
    }

    @Override
    public int obtenerTamaño() { return tamaño; }

    // ── Nodo interno ──────────────────────────────────────────────────────────

    private static class Nodo {
        Envio envio;
        Nodo  siguiente;
        Nodo(Envio e) { this.envio = e; }
    }

    // ── Iterador interno ──────────────────────────────────────────────────────

    private class IteradorLista implements IteradorEnvios {
        private Nodo actual = cabeza;

        @Override
        public boolean tieneSiguiente() { return actual != null; }

        @Override
        public Envio obtenerSiguiente() {
            Envio e = actual.envio;
            actual  = actual.siguiente;
            return e;
        }

        @Override
        public void reiniciar() { actual = cabeza; }
    }
}

