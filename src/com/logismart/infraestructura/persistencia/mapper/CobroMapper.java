package com.logismart.infraestructura.persistencia.mapper;

import com.logismart.dominio.empresa.Cobro;

/**
 * Data Mapper para Cobro (concepto Pago del sistema).
 * Persiste: id (VARCHAR), monto, estado, fecha, medioPago, envioId.
 * El campo envioId fue agregado aditivamente en Hito 13 para soportar buscarPorEnvio.
 * Patron: Data Mapper (Fowler, PoEAA).
 */
public interface CobroMapper {
    void insertar(Cobro cobro);
    void actualizar(Cobro cobro);
    void eliminar(String id);
    Cobro buscarPorId(String id);
}

