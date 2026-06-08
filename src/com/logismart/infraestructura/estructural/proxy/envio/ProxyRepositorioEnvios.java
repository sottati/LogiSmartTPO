package com.logismart.infraestructura.estructural.proxy.envio;

import com.logismart.dominio.envio.Envio;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProxyRepositorioEnvios implements RepositorioEnvios {
    private RepositorioEnviosReal repositorioReal;
    private final Map<String, Envio> cache = new HashMap<>();
    private List<Envio> cacheEnvios;
    private boolean cacheEnviosValido;
    private String usuarioActual = "operador-logistica";

    public void autenticarComo(String usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    public boolean estaInicializado() {
        return repositorioReal != null;
    }

    public int obtenerTamanoCache() {
        return cache.size();
    }

    public boolean isCacheEnviosValido() {
        return cacheEnviosValido;
    }

    private RepositorioEnviosReal obtenerRepositorio() {
        validarAcceso();
        if (repositorioReal == null) {
            System.out.println("[Proxy] Inicializando repositorio...");
            repositorioReal = new RepositorioEnviosReal();
        }
        return repositorioReal;
    }

    @Override
    public Envio obtenerEnvio(String id) {
        validarId(id);
        System.out.println("[Proxy] Buscando en cache: " + id);
        if (cache.containsKey(id)) {
            System.out.println("[Proxy] Encontrado en cache");
            return cache.get(id);
        }
        System.out.println("[Proxy] No esta en cache, consultando BD");
        Envio envio = obtenerRepositorio().obtenerEnvio(id);
        if (envio != null) {
            cache.put(id, envio);
        }
        return envio;
    }

    @Override
    public List<Envio> obtenerEnvios() {
        System.out.println("[Proxy] Verificando cache de envios");
        if (cacheEnviosValido) {
            System.out.println("[Proxy] Usando cache de lista");
            return new ArrayList<>(cacheEnvios);
        }
        System.out.println("[Proxy] Cache invalido, consultando BD");
        cacheEnvios = obtenerRepositorio().obtenerEnvios();
        cacheEnviosValido = true;
        return new ArrayList<>(cacheEnvios);
    }

    @Override
    public void guardarEnvio(Envio envio) {
        validarEnvio(envio);
        System.out.println("[Proxy] Guardando y actualizando cache");
        obtenerRepositorio().guardarEnvio(envio);
        cache.put(envio.getId(), envio);
        cacheEnviosValido = false;
    }

    @Override
    public void eliminarEnvio(String id) {
        validarId(id);
        System.out.println("[Proxy] Eliminando y actualizando cache");
        obtenerRepositorio().eliminarEnvio(id);
        cache.remove(id);
        cacheEnviosValido = false;
    }

    private void validarAcceso() {
        if (usuarioActual == null || usuarioActual.isBlank()) {
            throw new IllegalStateException("Acceso denegado al repositorio de envios");
        }
    }

    private void validarId(String id) {
        validarAcceso();
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del envio es obligatorio");
        }
    }

    private void validarEnvio(Envio envio) {
        validarAcceso();
        if (envio == null) {
            throw new IllegalArgumentException("El envio no puede ser nulo");
        }
    }
}

