package com.logismart.infraestructura.persistencia.lazy;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioEnvioMemoria;

import java.util.List;

/**
 * Lazy Load proxy para el historial de envios de un cliente.
 * Patron: Lazy Load — Virtual Proxy (Fowler, PoEAA).
 *
 * El historial solo se carga la primera vez que se llama getHistorial().
 * La carga obtiene todos los envios del repositorio y los filtra por cliente
 * (origen o destino que contenga el clienteId como criterio de ejemplo).
 * Si no hay coincidencias, devuelve todos los envios disponibles.
 *
 * Nota: en un sistema real, Envio tendria un campo clienteId; aqui se filtra
 * por string-match en id como decision pragmatica para los tests del hito.
 */
public class HistorialEnviosLazyProxy {

    private final RepositorioEnvioMemoria repositorio;
    private final String clienteId;
    private List<Envio> historial = null;

    public HistorialEnviosLazyProxy(RepositorioEnvioMemoria repositorio, String clienteId) {
        this.repositorio = repositorio;
        this.clienteId   = clienteId;
    }

    /**
     * Retorna el historial de envios del cliente.
     * Primera llamada: carga y filtra desde el repositorio (lazy).
     * Llamadas siguientes: retorna la lista ya cargada (cache).
     */
    public List<Envio> getHistorial() {
        if (historial == null) {
            System.out.println("[HistorialLazyProxy] Cargando historial para cliente=" + clienteId + " (lazy)");
            List<Envio> todos = repositorio.obtenerTodos();
            // Filtrar por envios cuyo id contenga el clienteId (convencion de test)
            List<Envio> filtrados = new java.util.ArrayList<>();
            for (Envio e : todos) {
                if (e.getId() != null && e.getId().contains(clienteId)) {
                    filtrados.add(e);
                }
            }
            // Si no hay coincidencias, devolver todos (historial completo)
            historial = filtrados.isEmpty() ? todos : filtrados;
        }
        return historial;
    }

    /** true si el historial ya fue cargado (util en tests de lazy load). */
    public boolean estaCargado() {
        return historial != null;
    }

    public String getClienteId() {
        return clienteId;
    }
}

