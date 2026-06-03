package com.logismart.infraestructura.persistencia.repositorio.memoria;

import com.logismart.dominio.Cobro;
import com.logismart.infraestructura.persistencia.repositorio.RepositorioPago;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion en memoria de RepositorioPago.
 * buscarPorEnvio usa el campo envioId agregado aditivamente a Cobro en Hito 13.
 */
public class RepositorioPagoMemoria implements RepositorioPago {

    private final Map<String, Cobro> almacen = new HashMap<>();

    @Override
    public void guardar(Cobro cobro) {
        almacen.put(cobro.getId(), cobro);
    }

    @Override
    public Cobro obtener(String id) {
        return almacen.get(id);
    }

    /** Firma generica int — no aplicable; retorna null. */
    @Override
    public Cobro obtener(int id) {
        return null;
    }

    @Override
    public List<Cobro> obtenerTodos() {
        return new ArrayList<>(almacen.values());
    }

    /** Firma generica int — no aplicable para String id; no-op. */
    @Override
    public void eliminar(int id) {
        // no-op
    }

    public void eliminar(String id) {
        almacen.remove(id);
    }

    @Override
    public List<Cobro> buscarPorEnvio(String envioId) {
        List<Cobro> resultado = new ArrayList<>();
        for (Cobro c : almacen.values()) {
            if (envioId != null && envioId.equals(c.getEnvioId())) {
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
