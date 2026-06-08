package com.logismart.infraestructura.persistencia.repositorio.memoria;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.persistencia.repositorio.RepositorioEnvio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion en memoria de RepositorioEnvio.
 * Usa HashMap interno para simular persistencia sin base de datos.
 * Es la implementacion testeada en CasosDePruebaHito13.
 *
 * Decision de diseno (Hito 13): los tests corren contra implementaciones en
 * memoria — deterministas, sin dependencias externas. Las implementaciones SQL
 * compilan pero no se ejecutan en el build (sin driver/BD disponibles).
 */
public class RepositorioEnvioMemoria implements RepositorioEnvio {

    private final Map<String, Envio> almacen = new HashMap<>();

    @Override
    public void guardar(Envio envio) {
        almacen.put(envio.getId(), envio);
    }

    /** Overload String id — fidelidad al dominio real. */
    @Override
    public Envio obtener(String id) {
        return almacen.get(id);
    }

    /** Firma generica int — no aplicable; retorna null. Documentada como divergencia de diseno. */
    @Override
    public Envio obtener(int id) {
        return null;
    }

    @Override
    public List<Envio> obtenerTodos() {
        return new ArrayList<>(almacen.values());
    }

    /** Firma generica int — no aplicable para String id; no-op. */
    @Override
    public void eliminar(int id) {
        // no-op: Envio usa String id
    }

    public void eliminar(String id) {
        almacen.remove(id);
    }

    @Override
    public List<Envio> buscarPorEstado(String estado) {
        List<Envio> resultado = new ArrayList<>();
        for (Envio e : almacen.values()) {
            if (estado != null && estado.equals(e.getEstado())) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    /** Limpia el almacen (util en tests). */
    public void limpiar() {
        almacen.clear();
    }

    public int tamanio() {
        return almacen.size();
    }
}

