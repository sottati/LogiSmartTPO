package com.logismart.infraestructura.persistencia.mapper;

import com.logismart.infraestructura.persistencia.entidad.CentroDistribucion;

/**
 * Data Mapper para CentroDistribucion (entidad de persistencia).
 * Opera sobre la entidad plana generada por CentroAssembler — no sobre el Composite.
 * Patron: Data Mapper (Fowler, PoEAA).
 */
public interface CentroDistribucionMapper {
    void insertar(CentroDistribucion centro);
    void actualizar(CentroDistribucion centro);
    void eliminar(String id);
    CentroDistribucion buscarPorId(String id);
}
