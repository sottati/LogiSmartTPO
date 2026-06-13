package com.logismart.persistencia;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.singleton.ConexionBD;
import com.logismart.infraestructura.singleton.Logger;
import java.util.ArrayList;
import java.util.List;

/**
 * Unit of Work: agrupa operaciones de BD en una transacción atómica.
 * Responsabilidad distinta a Memento: UoW gestiona atomicidad en BD;
 * Memento gestiona historial de estados en memoria del objeto Envio.
 */
public class UnitOfWork {

    private final RepositorioEnvio repositorio;
    private final EnvioMapper      mapper;
    private final ConexionBD       bd  = ConexionBD.obtenerInstancia();
    private final Logger           log = Logger.obtenerInstancia();

    private final List<Envio>  nuevos      = new ArrayList<>();
    private final List<Envio>  modificados = new ArrayList<>();
    private final List<String> eliminados  = new ArrayList<>();

    public UnitOfWork(RepositorioEnvio repositorio, EnvioMapper mapper) {
        this.repositorio = repositorio;
        this.mapper      = mapper;
    }

    public void registrarNuevo(Envio e)      { nuevos.add(e);        log.log("UoW: NUEVO " + e.getId()); }
    public void registrarModificado(Envio e) { modificados.add(e);   log.log("UoW: MODIFICADO " + e.getId()); }
    public void registrarEliminado(String id){ eliminados.add(id);   log.log("UoW: ELIMINADO " + id); }

    public boolean commit() {
        try {
            bd.ejecutarQuery("BEGIN TRANSACTION");
            for (Envio e : nuevos)      { mapper.insertar(e);         repositorio.guardar(e); }
            for (Envio e : modificados) { mapper.actualizar(e);        repositorio.guardar(e); }
            for (String id : eliminados){ mapper.eliminar(id);         repositorio.eliminar(id); }
            bd.ejecutarQuery("COMMIT");
            log.log("UoW: COMMIT OK — " + nuevos.size() + " nuevos, "
                    + modificados.size() + " modificados, " + eliminados.size() + " eliminados");
            limpiar();
            return true;
        } catch (Exception ex) {
            log.logError("UoW.commit", ex);
            rollback();
            return false;
        }
    }

    public void rollback() {
        bd.ejecutarQuery("ROLLBACK");
        log.log("UoW: ROLLBACK — " + pendientes() + " operaciones descartadas");
        limpiar();
    }

    private void limpiar() { nuevos.clear(); modificados.clear(); eliminados.clear(); }
    public int pendientes() { return nuevos.size() + modificados.size() + eliminados.size(); }
}
