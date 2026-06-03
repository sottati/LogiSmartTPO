package com.logismart.aplicacion.hito13;

import com.logismart.dominio.ClienteFinal;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioClienteMemoria;

import java.util.List;

/**
 * Servicio de aplicacion para ClienteFinal.
 * Coordina operaciones CRUD sobre el repositorio de clientes.
 */
public class ServicioClientes {

    private final RepositorioClienteMemoria repositorio;

    public ServicioClientes(RepositorioClienteMemoria repositorio) {
        this.repositorio = repositorio;
    }

    public void crearCliente(ClienteFinal cliente) {
        repositorio.guardar(cliente);
        System.out.println("[ServicioClientes] Cliente creado: " + cliente.getId());
    }

    public ClienteFinal obtenerCliente(String id) {
        return repositorio.obtener(id);
    }

    public void actualizarCliente(ClienteFinal cliente) {
        repositorio.guardar(cliente);
        System.out.println("[ServicioClientes] Cliente actualizado: " + cliente.getId());
    }

    public List<ClienteFinal> listarClientes() {
        return repositorio.obtenerTodos();
    }
}
