package com.logismart.infraestructura.persistencia.repositorio.memoria;

import com.logismart.infraestructura.persistencia.entidad.CentroDistribucionEntity;
import com.logismart.infraestructura.persistencia.repositorio.RepositorioCentro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion en memoria de RepositorioCentro.
 * Usa HashMap<String, CentroDistribucionEntity> internamente.
 * La entidad de persistencia CentroDistribucionEntity usa String id (generado por el sistema).
 * La interfaz generica usa int id; se mapea via String.valueOf.
 */
public class RepositorioCentroMemoria implements RepositorioCentro {

    private final Map<String, CentroDistribucionEntity> almacen = new HashMap<>();

    @Override
    public void guardar(CentroDistribucionEntity centro) {
        almacen.put(centro.getId(), centro);
    }

    @Override
    public CentroDistribucionEntity obtener(int id) {
        return almacen.get(String.valueOf(id));
    }

    /** Overload String id — conveniente para el uso interno. */
    public CentroDistribucionEntity obtener(String id) {
        return almacen.get(id);
    }

    @Override
    public List<CentroDistribucionEntity> obtenerTodos() {
        return new ArrayList<>(almacen.values());
    }

    @Override
    public void eliminar(int id) {
        almacen.remove(String.valueOf(id));
    }

    public void eliminar(String id) {
        almacen.remove(id);
    }

    @Override
    public List<CentroDistribucionEntity> buscarPorUbicacion(String ubicacion) {
        List<CentroDistribucionEntity> resultado = new ArrayList<>();
        for (CentroDistribucionEntity c : almacen.values()) {
            if (ubicacion != null && ubicacion.equalsIgnoreCase(c.getUbicacion())) {
                resultado.add(c);
            }
        }
        return resultado;
    }

    public void limpiar() {
        almacen.clear();
    }

    public int tamanio() {
        return almacen.size();
    }
}
