package com.logismart.infraestructura.persistencia.mapper;

import com.logismart.dominio.usuario.ClienteFinal;

/**
 * Data Mapper para ClienteFinal.
 * Persiste el subconjunto relevante: id (VARCHAR), nombre, email, telefono.
 * Ignora passwordHash, rol, estado y permisos (responsabilidad del modulo de autenticacion).
 * Patron: Data Mapper (Fowler, PoEAA).
 */
public interface ClienteMapper {
    void insertar(ClienteFinal cliente);
    void actualizar(ClienteFinal cliente);
    void eliminar(String id);
    ClienteFinal buscarPorId(String id);
}

