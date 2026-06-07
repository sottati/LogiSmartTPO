package com.logismart.aplicacion.hito13;

import com.logismart.infraestructura.persistencia.entidad.CentroDistribucionEntity;
import com.logismart.infraestructura.persistencia.repositorio.memoria.RepositorioCentroMemoria;

import java.util.List;

/**
 * Servicio de aplicacion para CentroDistribucionEntity (entidad de persistencia).
 * Opera sobre la entidad plana; las instancias Composite del dominio se
 * convierten previamente via CentroAssembler antes de pasarlas aqui.
 */
public class ServicioCentros {

    private final RepositorioCentroMemoria repositorio;

    public ServicioCentros(RepositorioCentroMemoria repositorio) {
        this.repositorio = repositorio;
    }

    public void crearCentro(CentroDistribucionEntity centro) {
        repositorio.guardar(centro);
        System.out.println("[ServicioCentros] Centro creado: " + centro.getId());
    }

    public CentroDistribucionEntity obtenerCentro(String id) {
        return repositorio.obtener(id);
    }

    public void actualizarCentro(CentroDistribucionEntity centro) {
        repositorio.guardar(centro);
        System.out.println("[ServicioCentros] Centro actualizado: " + centro.getId());
    }

    public List<CentroDistribucionEntity> listarCentros() {
        return repositorio.obtenerTodos();
    }
}
