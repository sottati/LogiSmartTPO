package com.logismart.infraestructura.persistencia.mapper;

import com.logismart.dominio.Envio;

/**
 * Data Mapper para Envio.
 * Desacopla el modelo de dominio Envio de la logica de acceso a datos.
 * Patron: Data Mapper (Fowler, PoEAA).
 */
public interface EnvioMapper {
    void insertar(Envio envio);
    void actualizar(Envio envio);
    void eliminar(String id);
    Envio buscarPorId(String id);
}
