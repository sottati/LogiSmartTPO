package com.logismart.infraestructura.persistencia.repositorio;

import com.logismart.infraestructura.persistencia.entidad.CentroDistribucion;

import java.util.List;

/**
 * Repositorio especifico para CentroDistribucion (entidad de persistencia).
 * Usa int id de la interfaz generica directamente (coherente con el plan).
 * Agrega buscarPorUbicacion como operacion propia del dominio logistico.
 */
public interface RepositorioCentro extends Repositorio<CentroDistribucion> {
    List<CentroDistribucion> buscarPorUbicacion(String ubicacion);
}
