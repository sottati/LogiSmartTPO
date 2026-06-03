package com.logismart.aplicacion.hito13;

import com.logismart.dominio.Cobro;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioPagoMemoria;

import java.util.List;

/**
 * Servicio de aplicacion para Cobro (Pago).
 * Coordina operaciones CRUD y busqueda por envio sobre el repositorio de pagos.
 */
public class ServicioPagos {

    private final RepositorioPagoMemoria repositorio;

    public ServicioPagos(RepositorioPagoMemoria repositorio) {
        this.repositorio = repositorio;
    }

    public void crearPago(Cobro cobro) {
        repositorio.guardar(cobro);
        System.out.println("[ServicioPagos] Pago creado: " + cobro.getId());
    }

    public Cobro obtenerPago(String id) {
        return repositorio.obtener(id);
    }

    public void actualizarPago(Cobro cobro) {
        repositorio.guardar(cobro);
        System.out.println("[ServicioPagos] Pago actualizado: " + cobro.getId());
    }

    public List<Cobro> listarPagos() {
        return repositorio.obtenerTodos();
    }

    public List<Cobro> buscarPagosPorEnvio(String envioId) {
        return repositorio.buscarPorEnvio(envioId);
    }
}
