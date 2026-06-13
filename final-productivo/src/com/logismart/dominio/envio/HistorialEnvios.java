package com.logismart.dominio.envio;

import java.util.ArrayList;
import java.util.List;

/**
 * Cuidador (Caretaker) del patrón Memento.
 *
 * Almacena snapshots del ciclo de vida de un Envío para auditoría y analítica
 * (p. ej. tiempo promedio entre estados). Diferenciado del Unit of Work:
 * éste registra historial en memoria; UoW garantiza atomicidad en base de datos.
 *
 * Soporta navegación bidireccional porque los analistas necesitan consultar
 * cualquier punto del historial, no sólo el último.
 */
public class HistorialEnvios {

    private final List<MementoEnvio> historial    = new ArrayList<>();
    private int                      indiceActual = -1;

    public void guardarEstado(Envio envio) {
        while (indiceActual < historial.size() - 1) {
            historial.remove(historial.size() - 1);
        }
        historial.add(envio.crearMemento());
        indiceActual++;
    }

    public void irAlEstadoAnterior(Envio envio) {
        if (indiceActual > 0) {
            indiceActual--;
            envio.restaurarDesdeMemento(historial.get(indiceActual));
        }
    }

    public void irAlEstadoSiguiente(Envio envio) {
        if (indiceActual < historial.size() - 1) {
            indiceActual++;
            envio.restaurarDesdeMemento(historial.get(indiceActual));
        }
    }

    public void irAlEstado(int indice, Envio envio) {
        if (indice >= 0 && indice < historial.size()) {
            indiceActual = indice;
            envio.restaurarDesdeMemento(historial.get(indiceActual));
        }
    }

    public List<MementoEnvio> obtenerHistorial() {
        return new ArrayList<>(historial);
    }

    public int obtenerTamaño()       { return historial.size(); }
    public int obtenerIndiceActual() { return indiceActual; }
}
