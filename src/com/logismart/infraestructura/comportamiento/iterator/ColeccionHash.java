package com.logismart.infraestructura.comportamiento.iterator;

import com.logismart.dominio.envio.Envio;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Colección basada en tabla hash (búsqueda O(1) por ID).
 * Uso típico: lookup rápido de un envío específico por identificador.
 *
 * El iterador interno (IteradorHash) delega en el iterador nativo del Map,
 * pero lo envuelve en la interfaz IteradorEnvios para uniformidad.
 * El reiniciar() recrea el iterador nativo desde cero.
 *
 * Patrón: Iterator (GoF) - Hito 11
 */
public class ColeccionHash implements ColeccionEnvios {

    private final Map<String, Envio> envios = new HashMap<>();

    @Override
    public IteradorEnvios crearIterador() {
        return new IteradorHash();
    }

    @Override
    public void agregar(Envio envio) {
        envios.put(envio.getId(), envio);
    }

    @Override
    public void remover(Envio envio) {
        envios.remove(envio.getId());
    }

    @Override
    public int obtenerTamaño() { return envios.size(); }

    // ── Iterador interno ──────────────────────────────────────────────────────

    private class IteradorHash implements IteradorEnvios {
        private Iterator<Envio> cursor = envios.values().iterator();

        @Override
        public boolean tieneSiguiente() { return cursor.hasNext(); }

        @Override
        public Envio obtenerSiguiente() { return cursor.next(); }

        @Override
        public void reiniciar() { cursor = envios.values().iterator(); }
    }
}

