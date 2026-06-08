package com.logismart.infraestructura.persistencia.repositorio;

import com.logismart.dominio.usuario.ClienteFinal;

import java.util.List;

/**
 * Repositorio especifico para ClienteFinal.
 * Overload obtener(String id): ClienteFinal usa String como identificador.
 */
public interface RepositorioCliente extends Repositorio<ClienteFinal> {
    ClienteFinal obtener(String id);
    List<ClienteFinal> buscarPorNombre(String nombre);
}

