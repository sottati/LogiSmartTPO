package com.logismart.infraestructura.persistencia.lazy;

import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioClienteMemoria;

/**
 * Lazy Load proxy para ClienteFinal.
 * Patron: Lazy Load — Virtual Proxy (Fowler, PoEAA).
 *
 * El cliente solo se carga desde el repositorio al primer acceso getCliente().
 * Llamadas posteriores devuelven la instancia ya cargada (sin ir al repositorio).
 *
 * Uso tipico: en un Envio o en un agregado que referencia un ClienteFinal,
 * se almacena el id del cliente en vez del objeto completo; el proxy carga
 * el objeto solo si se necesita durante el procesamiento.
 */
public class ClienteLazyProxy {

    private final RepositorioClienteMemoria repositorio;
    private final String clienteId;
    private ClienteFinal clienteCargado = null;

    public ClienteLazyProxy(RepositorioClienteMemoria repositorio, String clienteId) {
        this.repositorio = repositorio;
        this.clienteId   = clienteId;
    }

    /**
     * Retorna el ClienteFinal asociado al clienteId.
     * Primera llamada: carga desde el repositorio (lazy).
     * Llamadas siguientes: retorna la instancia ya cargada (cache).
     */
    public ClienteFinal getCliente() {
        if (clienteCargado == null) {
            System.out.println("[ClienteLazyProxy] Cargando cliente id=" + clienteId + " (lazy)");
            clienteCargado = repositorio.obtener(clienteId);
        }
        return clienteCargado;
    }

    /** true si el cliente ya fue cargado (util en tests de lazy load). */
    public boolean estaCargado() {
        return clienteCargado != null;
    }

    public String getClienteId() {
        return clienteId;
    }
}

