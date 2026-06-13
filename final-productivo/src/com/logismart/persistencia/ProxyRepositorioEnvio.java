package com.logismart.persistencia;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.singleton.Logger;
import java.util.*;

/**
 * Proxy + Lazy Load sobre RepositorioEnvio.
 * Cache por id (TTL=60s) + cache de lista completa invalidada en escrituras.
 */
public class ProxyRepositorioEnvio implements RepositorioEnvio {

    private static final long TTL_MS = 60_000;

    private final RepositorioEnvio real;
    private final Logger           log = Logger.obtenerInstancia();

    private final Map<String, Envio> cacheId       = new HashMap<>();
    private final Map<String, Long>  cacheTiempoId = new HashMap<>();
    private List<Envio>              cacheTotal     = null;

    public ProxyRepositorioEnvio(RepositorioEnvio real) { this.real = real; }

    @Override
    public void guardar(Envio e) {
        real.guardar(e);
        cacheId.put(e.getId(), e);
        cacheTiempoId.put(e.getId(), System.currentTimeMillis());
        cacheTotal = null;
        log.log("Proxy: guardar " + e.getId() + " — cache total invalidado");
    }

    @Override
    public Optional<Envio> obtener(String id) {
        Long ts = cacheTiempoId.get(id);
        if (ts != null && (System.currentTimeMillis() - ts) < TTL_MS) {
            log.log("Proxy: HIT cache para " + id);
            return Optional.ofNullable(cacheId.get(id));
        }
        log.log("Proxy: MISS cache para " + id + " — delegando al repositorio real");
        Optional<Envio> resultado = real.obtener(id);
        resultado.ifPresent(e -> {
            cacheId.put(id, e);
            cacheTiempoId.put(id, System.currentTimeMillis());
        });
        return resultado;
    }

    @Override
    public List<Envio> obtenerTodos() {
        if (cacheTotal != null) { log.log("Proxy: HIT cache total"); return new ArrayList<>(cacheTotal); }
        log.log("Proxy: MISS cache total — cargando desde repositorio real");
        cacheTotal = real.obtenerTodos();
        return new ArrayList<>(cacheTotal);
    }

    @Override
    public void eliminar(String id) {
        real.eliminar(id);
        cacheId.remove(id);
        cacheTiempoId.remove(id);
        cacheTotal = null;
        log.log("Proxy: eliminar " + id + " — cache total invalidado");
    }

    @Override public List<Envio> buscarPorEstado(String estado)     { return real.buscarPorEstado(estado); }
    @Override public List<Envio> buscarPorEmpresa(String empresaId) { return real.buscarPorEmpresa(empresaId); }

    public void invalidarCache() { cacheId.clear(); cacheTiempoId.clear(); cacheTotal = null; }
    public int  tamanioCache()   { return cacheId.size(); }
}
