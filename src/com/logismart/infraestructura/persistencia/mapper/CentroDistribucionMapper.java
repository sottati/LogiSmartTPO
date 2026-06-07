package com.logismart.infraestructura.persistencia.mapper;

import com.logismart.infraestructura.persistencia.entidad.CentroDistribucionEntity;

/**
 * Data Mapper para CentroDistribucionEntity (entidad de persistencia).
 * Opera sobre la entidad plana generada por CentroAssembler — no sobre el Composite.
 * Patron: Data Mapper (Fowler, PoEAA).
 */
public interface CentroDistribucionMapper {
    void insertar(CentroDistribucionEntity centro);
    void actualizar(CentroDistribucionEntity centro);
    void eliminar(String id);
    CentroDistribucionEntity buscarPorId(String id);
}
