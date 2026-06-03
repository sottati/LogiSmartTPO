package com.logismart.infraestructura.persistencia.repositorio;

import java.util.List;

/**
 * Interfaz generica del patron Repository.
 * Abstrae el acceso a colecciones de objetos del dominio, independizando
 * al cliente de la implementacion de almacenamiento (memoria, SQL, etc.).
 * Patron: Repository (Fowler, PoEAA).
 *
 * Nota de diseno (Hito 13): el id generico es int para la interfaz base.
 * Las entidades con String id (Envio, ClienteFinal, Cobro) extienden esta
 * interfaz y agregan overloads obtener(String id) — decision documentada.
 */
public interface Repositorio<T> {
    void guardar(T entidad);
    T obtener(int id);
    List<T> obtenerTodos();
    void eliminar(int id);
}
