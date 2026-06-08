package com.logismart.aplicacion;

import com.logismart.dominio.envio.Envio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * GRASP Pure Fabrication: Clase artificial que centraliza la persistencia de Envios.
 * La responsabilidad de almacenar y recuperar envios no pertenece ni a Envio
 * (que es una entidad de dominio) ni a Logismart (que es la fachada del sistema).
 *
 * En produccion esta clase delegaria en un ORM o una base de datos.
 * En este contexto academico usa un Map en memoria como store.
 */
public class RepositorioDeEnvios {

    private final Map<String, Envio> store = new LinkedHashMap<>();

    public void guardar(Envio envio) {
        if (envio == null) {
            throw new IllegalArgumentException("El envio no puede ser nulo");
        }
        store.put(envio.getId(), envio);
    }

    public Optional<Envio> buscarPorId(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Envio> listarTodos() {
        return Collections.unmodifiableList(new ArrayList<>(store.values()));
    }

    public List<Envio> listarPorEstado(String estado) {
        List<Envio> resultado = new ArrayList<>();
        for (Envio envio : store.values()) {
            if (estado.equalsIgnoreCase(envio.getEstado())) {
                resultado.add(envio);
            }
        }
        return Collections.unmodifiableList(resultado);
    }

    public void eliminar(String id) {
        store.remove(id);
    }

    public boolean existe(String id) {
        return store.containsKey(id);
    }

    public int total() {
        return store.size();
    }
}

