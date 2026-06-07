package com.logismart.infraestructura.persistencia.lazy;

import com.logismart.infraestructura.persistencia.entidad.CentroDistribucionEntity;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioCentroMemoria;

/**
 * Lazy Load proxy para CentroDistribucionEntity (entidad de persistencia).
 * Patron: Lazy Load — Virtual Proxy (Fowler, PoEAA).
 *
 * El centro solo se carga desde el repositorio al primer acceso getCentro().
 * Evita cargar toda la estructura de centros cuando solo se necesita el id.
 */
public class CentroDistribucionLazyProxy {

    private final RepositorioCentroMemoria repositorio;
    private final String centroId;
    private CentroDistribucionEntity centroCargado = null;

    public CentroDistribucionLazyProxy(RepositorioCentroMemoria repositorio, String centroId) {
        this.repositorio = repositorio;
        this.centroId    = centroId;
    }

    /**
     * Retorna el CentroDistribucionEntity asociado al centroId.
     * Primera llamada: carga desde el repositorio (lazy).
     * Llamadas siguientes: retorna la instancia ya cargada (cache).
     */
    public CentroDistribucionEntity getCentro() {
        if (centroCargado == null) {
            System.out.println("[CentroLazyProxy] Cargando centro id=" + centroId + " (lazy)");
            centroCargado = repositorio.obtener(centroId);
        }
        return centroCargado;
    }

    /** true si el centro ya fue cargado (util en tests de lazy load). */
    public boolean estaCargado() {
        return centroCargado != null;
    }

    public String getCentroId() {
        return centroId;
    }
}
