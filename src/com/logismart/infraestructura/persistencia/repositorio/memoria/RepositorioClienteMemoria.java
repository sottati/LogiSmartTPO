package com.logismart.infraestructura.persistencia.repositorio.memoria;

import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.infraestructura.persistencia.repositorio.RepositorioCliente;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementacion en memoria de RepositorioCliente.
 * Usa HashMap interno. Es la implementacion testeada en CasosDePruebaHito13.
 */
public class RepositorioClienteMemoria implements RepositorioCliente {

    private final Map<String, ClienteFinal> almacen = new HashMap<>();

    @Override
    public void guardar(ClienteFinal cliente) {
        almacen.put(cliente.getId(), cliente);
    }

    @Override
    public ClienteFinal obtener(String id) {
        return almacen.get(id);
    }

    /** Firma generica int — no aplicable; retorna null. */
    @Override
    public ClienteFinal obtener(int id) {
        return null;
    }

    @Override
    public List<ClienteFinal> obtenerTodos() {
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
    public List<ClienteFinal> buscarPorNombre(String nombre) {
        List<ClienteFinal> resultado = new ArrayList<>();
        for (ClienteFinal c : almacen.values()) {
            if (nombre != null && nombre.equalsIgnoreCase(c.getNombre())) {
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

