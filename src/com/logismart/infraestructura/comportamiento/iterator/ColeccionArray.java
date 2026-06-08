package com.logismart.infraestructura.comportamiento.iterator;

import com.logismart.dominio.envio.Envio;

/**
 * Colección basada en array de tamaño fijo.
 * Uso típico: repositorio de envíos activos con acceso O(1) por índice.
 *
 * El iterador interno (IteradorArray) encapsula el cursor de posición;
 * el cliente nunca accede al array directamente.
 *
 * Patrón: Iterator (GoF) - Hito 11
 */
public class ColeccionArray implements ColeccionEnvios {

    private static final int CAPACIDAD_MAX = 1000;
    private final Envio[] envios = new Envio[CAPACIDAD_MAX];
    private int tamaño = 0;

    @Override
    public IteradorEnvios crearIterador() {
        return new IteradorArray();
    }

    @Override
    public void agregar(Envio envio) {
        if (tamaño < CAPACIDAD_MAX) {
            envios[tamaño++] = envio;
        }
    }

    @Override
    public void remover(Envio envio) {
        for (int i = 0; i < tamaño; i++) {
            if (envios[i].getId().equals(envio.getId())) {
                System.arraycopy(envios, i + 1, envios, i, tamaño - i - 1);
                envios[--tamaño] = null;
                return;
            }
        }
    }

    @Override
    public int obtenerTamaño() { return tamaño; }

    // ── Iterador interno ──────────────────────────────────────────────────────

    private class IteradorArray implements IteradorEnvios {
        private int indice = 0;

        @Override
        public boolean tieneSiguiente() { return indice < tamaño; }

        @Override
        public Envio obtenerSiguiente() { return envios[indice++]; }

        @Override
        public void reiniciar() { indice = 0; }
    }
}

