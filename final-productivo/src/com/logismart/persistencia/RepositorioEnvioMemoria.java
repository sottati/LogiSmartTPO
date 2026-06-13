package com.logismart.persistencia;

import com.logismart.dominio.envio.Envio;
import java.util.*;
import java.util.stream.Collectors;

public class RepositorioEnvioMemoria implements RepositorioEnvio {
    private final Map<String, Envio> almacen = new HashMap<>();

    @Override public void        guardar(Envio e)    { almacen.put(e.getId(), e); }
    @Override public void        eliminar(String id) { almacen.remove(id); }
    @Override public Optional<Envio> obtener(String id) { return Optional.ofNullable(almacen.get(id)); }
    @Override public List<Envio> obtenerTodos()      { return new ArrayList<>(almacen.values()); }

    @Override
    public List<Envio> buscarPorEstado(String estado) {
        return almacen.values().stream()
                .filter(e -> estado.equals(e.getEstado()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Envio> buscarPorEmpresa(String empresaId) {
        return almacen.values().stream()
                .filter(e -> e.getEmpresa() != null && empresaId.equals(e.getEmpresa().getId()))
                .collect(Collectors.toList());
    }

    public int  tamanio() { return almacen.size(); }
    public void limpiar() { almacen.clear(); }
}
