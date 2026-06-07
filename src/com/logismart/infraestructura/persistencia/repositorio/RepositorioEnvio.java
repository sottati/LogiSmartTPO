package com.logismart.infraestructura.persistencia.repositorio;

import com.logismart.dominio.envio.Envio;

import java.util.List;

/**
 * Repositorio especifico para Envio.
 * Extiende Repositorio<Envio> y agrega operaciones de busqueda propias del dominio.
 *
 * Overload obtener(String id): Envio usa String como identificador (fidelidad al dominio).
 * La firma int del padre queda reservada para compatibilidad con la interfaz generica.
 */
public interface RepositorioEnvio extends Repositorio<Envio> {
    Envio obtener(String id);
    List<Envio> buscarPorEstado(String estado);
}

