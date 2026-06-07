package com.logismart.infraestructura.persistencia.repositorio;

import com.logismart.infraestructura.persistencia.entidad.CentroDistribucionEntity;

import java.util.List;

/**
 * Repositorio especifico para CentroDistribucionEntity (entidad de persistencia).
 * Usa int id de la interfaz generica directamente (coherente con el plan).
 * Agrega buscarPorUbicacion como operacion propia del dominio logistico.
 */
public interface RepositorioCentro extends Repositorio<CentroDistribucionEntity> {
    List<CentroDistribucionEntity> buscarPorUbicacion(String ubicacion);
}
