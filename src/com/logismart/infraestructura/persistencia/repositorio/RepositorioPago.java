package com.logismart.infraestructura.persistencia.repositorio;

import com.logismart.dominio.empresa.Cobro;

import java.util.List;

/**
 * Repositorio especifico para Cobro (concepto Pago del sistema).
 * Overload obtener(String id): Cobro usa String como identificador.
 * buscarPorEnvio: habilita la trazabilidad pago → envio usando el campo
 * envioId agregado aditivamente a Cobro en Hito 13.
 */
public interface RepositorioPago extends Repositorio<Cobro> {
    Cobro obtener(String id);
    List<Cobro> buscarPorEnvio(String envioId);
}

