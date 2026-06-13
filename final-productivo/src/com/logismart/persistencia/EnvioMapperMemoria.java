package com.logismart.persistencia;

import com.logismart.dominio.envio.Envio;
import java.util.*;

/** In-memory Data Mapper: mapea objetos Envio a un almacén simulado (Map). */
public class EnvioMapperMemoria extends EnvioMapper {
    private final Map<String, Map<String, Object>> tabla = new HashMap<>();

    @Override
    public void insertar(Envio envio) {
        super.insertar(envio);
        Map<String, Object> fila = new LinkedHashMap<>();
        fila.put("id",      envio.getId());
        fila.put("origen",  envio.getOrigen());
        fila.put("destino", envio.getDestino());
        fila.put("estado",  envio.getEstado());
        fila.put("peso",    envio.getPeso());
        fila.put("costo",   envio.getCosto());
        tabla.put(envio.getId(), fila);
    }

    @Override
    public void actualizar(Envio envio) {
        super.actualizar(envio);
        Map<String, Object> fila = tabla.get(envio.getId());
        if (fila != null) {
            fila.put("estado", envio.getEstado());
            fila.put("costo",  envio.getCosto());
        }
    }

    @Override
    public void eliminar(String id) {
        super.eliminar(id);
        tabla.remove(id);
    }

    @Override
    public Optional<Envio> buscarPorId(String id) {
        super.buscarPorId(id);
        return Optional.empty();
    }

    public boolean existe(String id)        { return tabla.containsKey(id); }
    public int     cantidadFilas()          { return tabla.size(); }
    public String  obtenerEstado(String id) {
        Map<String, Object> fila = tabla.get(id);
        return fila == null ? null : (String) fila.get("estado");
    }
}
