package com.logismart.infraestructura.persistencia.unitofwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit of Work — agrupa operaciones sobre el dominio en una unidad transaccional.
 * Patron: Unit of Work (Fowler, PoEAA).
 *
 * Funciona como un "diario de cambios": los objetos se registran como nuevos,
 * modificados o eliminados. Al llamar commit() se ejecutan todas las operaciones
 * en orden (insert → update → delete) simulando una transaccion atomica.
 * rollback() descarta todos los cambios pendientes sin ejecutarlos.
 *
 * Implementacion: sin base de datos real; las operaciones se imprimen en consola
 * (doble de prueba). En produccion, commit() delegaria en los mappers SQL dentro
 * de una transaccion JDBC (connection.setAutoCommit(false) / commit / rollback).
 */
public class UnitOfWork {

    private final List<Object> nuevos      = new ArrayList<>();
    private final List<Object> modificados = new ArrayList<>();
    private final List<Object> eliminados  = new ArrayList<>();

    /** Registra un objeto para INSERT en el proximo commit. */
    public void registrarNuevo(Object entidad) {
        nuevos.add(entidad);
        System.out.println("[UnitOfWork] Registrado nuevo: " + entidad.getClass().getSimpleName());
    }

    /** Registra un objeto para UPDATE en el proximo commit. */
    public void registrarModificado(Object entidad) {
        modificados.add(entidad);
        System.out.println("[UnitOfWork] Registrado modificado: " + entidad.getClass().getSimpleName());
    }

    /** Registra un objeto para DELETE en el proximo commit. */
    public void registrarEliminado(Object entidad) {
        eliminados.add(entidad);
        System.out.println("[UnitOfWork] Registrado eliminado: " + entidad.getClass().getSimpleName());
    }

    /**
     * Ejecuta todas las operaciones pendientes en una sola unidad logica.
     * Orden: INSERT → UPDATE → DELETE (garantiza integridad referencial).
     * Luego vacia las listas — el UoW queda listo para el siguiente ciclo.
     */
    public void commit() {
        System.out.println("[UnitOfWork] ── commit() iniciado ────────────────");
        for (Object obj : nuevos) {
            System.out.println("  [INSERT] " + obj.getClass().getSimpleName() + ": " + obj);
        }
        for (Object obj : modificados) {
            System.out.println("  [UPDATE] " + obj.getClass().getSimpleName() + ": " + obj);
        }
        for (Object obj : eliminados) {
            System.out.println("  [DELETE] " + obj.getClass().getSimpleName() + ": " + obj);
        }
        int total = nuevos.size() + modificados.size() + eliminados.size();
        limpiar();
        System.out.println("[UnitOfWork] ── commit() completado (" + total + " operaciones) ─");
    }

    /**
     * Descarta todos los cambios pendientes sin ejecutarlos.
     * Equivalente a un rollback de transaccion.
     */
    public void rollback() {
        int descartados = nuevos.size() + modificados.size() + eliminados.size();
        limpiar();
        System.out.println("[UnitOfWork] rollback() — " + descartados + " cambios descartados.");
    }

    private void limpiar() {
        nuevos.clear();
        modificados.clear();
        eliminados.clear();
    }

    // Consultas de estado (utiles en tests)

    public int cantidadNuevos()      { return nuevos.size(); }
    public int cantidadModificados() { return modificados.size(); }
    public int cantidadEliminados()  { return eliminados.size(); }
    public int cantidadTotal()       { return nuevos.size() + modificados.size() + eliminados.size(); }
}
