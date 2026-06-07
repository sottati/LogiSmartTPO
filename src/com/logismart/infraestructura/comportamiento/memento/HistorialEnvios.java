package com.logismart.infraestructura.comportamiento.memento;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.MementoEnvio;

import java.util.ArrayList;
import java.util.List;

/**
 * Cuidador (Caretaker) del patrón Memento.
 *
 * Almacena snapshots del estado de un Envío y permite navegar su historial
 * en ambas direcciones (anterior/siguiente). Implementa navegación bidireccional
 * (no sólo stack LIFO) porque LogiSmart necesita auditar cualquier punto del
 * ciclo de vida de un envío, no sólo deshacer el último estado.
 *
 * El Cuidador nunca accede al contenido interno del MementoEnvio: sólo
 * almacena y entrega snapshots al Originador (Envio).
 *
 * Patrón: Memento (GoF) - Hito 11
 */
public class HistorialEnvios {

    private final List<MementoEnvio> historial    = new ArrayList<>();
    private int                      indiceActual = -1;

    /**
     * Guarda el estado actual del envío.
     * Si el cursor no está al final (hubo navegación hacia atrás), descarta
     * los estados posteriores antes de insertar (comportamiento tipo editor).
     */
    public void guardarEstado(Envio envio) {
        // Descartar estados futuros si navegamos hacia atrás y luego guardamos
        while (indiceActual < historial.size() - 1) {
            historial.remove(historial.size() - 1);
        }
        historial.add(envio.crearMemento());
        indiceActual++;
        System.out.println("✓ Estado guardado [" + indiceActual + "]: "
                + envio.obtenerEstado());
    }

    /**
     * Restaura el estado inmediatamente anterior al actual.
     * Si ya está en el primer estado, no hace nada.
     */
    public void irAlEstadoAnterior(Envio envio) {
        if (indiceActual > 0) {
            indiceActual--;
            envio.restaurarDesdeMemento(historial.get(indiceActual));
            System.out.println("✓ Restaurado a [" + indiceActual + "]: "
                    + envio.obtenerEstado());
        } else {
            System.out.println("  Ya está en el estado más antiguo");
        }
    }

    /**
     * Avanza al estado siguiente (redo).
     * Si ya está en el último estado, no hace nada.
     */
    public void irAlEstadoSiguiente(Envio envio) {
        if (indiceActual < historial.size() - 1) {
            indiceActual++;
            envio.restaurarDesdeMemento(historial.get(indiceActual));
            System.out.println("✓ Avanzado a [" + indiceActual + "]: "
                    + envio.obtenerEstado());
        } else {
            System.out.println("  Ya está en el estado más reciente");
        }
    }

    /** Restaura directamente a un índice específico del historial. */
    public void irAlEstado(int indice, Envio envio) {
        if (indice >= 0 && indice < historial.size()) {
            indiceActual = indice;
            envio.restaurarDesdeMemento(historial.get(indiceActual));
            System.out.println("✓ Restaurado a posición [" + indiceActual + "]: "
                    + envio.obtenerEstado());
        }
    }

    public void mostrarHistorial() {
        System.out.println("\n=== Historial de Estados (" + historial.size() + " entradas) ===");
        for (int i = 0; i < historial.size(); i++) {
            String marca = (i == indiceActual) ? "→" : " ";
            System.out.println(marca + " [" + i + "] " + historial.get(i).obtenerEstado());
        }
    }

    public int obtenerTamaño()      { return historial.size(); }
    public int obtenerIndiceActual() { return indiceActual;     }
}

