package com.logismart.persistencia;

import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.infraestructura.singleton.Logger;

/**
 * Lazy Load proxy para ClienteFinal.
 * La identidad (id, username, email) se conoce desde el inicio;
 * datos pesados (historial, categoría) se cargan bajo demanda al primer acceso.
 */
public class ClienteLazyProxy extends ClienteFinal {

    private final Logger log = Logger.obtenerInstancia();

    private boolean cargado            = false;
    private int     cantidadEnvios     = 0;
    private String  categoriaFidelidad = "ESTANDAR";

    public ClienteLazyProxy(String id, String username, String email) {
        super(id, username, email, "***lazy***", "ACTIVO");
    }

    private void cargarSiNecesario() {
        if (!cargado) {
            log.log("LazyLoad: cargando datos completos de cliente " + getId());
            cantidadEnvios     = simularConsultaBD(getId());
            categoriaFidelidad = cantidadEnvios >= 10 ? "PREMIUM" : "ESTANDAR";
            cargado = true;
        }
    }

    private int simularConsultaBD(String id) { return Math.abs(id.hashCode() % 20) + 1; }

    public int     getCantidadEnvios()    { cargarSiNecesario(); return cantidadEnvios; }
    public String  getCategoriaFidelidad(){ cargarSiNecesario(); return categoriaFidelidad; }
    public boolean estaCargado()          { return cargado; }
}
