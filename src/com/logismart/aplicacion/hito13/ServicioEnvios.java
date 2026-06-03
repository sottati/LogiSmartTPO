package com.logismart.aplicacion.hito13;

import com.logismart.dominio.Envio;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioEnvioMemoria;

import java.util.List;

/**
 * Servicio de aplicacion para Envios.
 * Coordina operaciones CRUD sobre el repositorio de envios.
 * Capa de aplicacion (no contiene logica de dominio).
 */
public class ServicioEnvios {

    private final RepositorioEnvioMemoria repositorio;

    public ServicioEnvios(RepositorioEnvioMemoria repositorio) {
        this.repositorio = repositorio;
    }

    public void crearEnvio(Envio envio) {
        repositorio.guardar(envio);
        System.out.println("[ServicioEnvios] Envio creado: " + envio.getId());
    }

    public Envio obtenerEnvio(String id) {
        return repositorio.obtener(id);
    }

    public void actualizarEnvio(Envio envio) {
        repositorio.guardar(envio);
        System.out.println("[ServicioEnvios] Envio actualizado: " + envio.getId());
    }

    public List<Envio> listarEnvios() {
        return repositorio.obtenerTodos();
    }
}
