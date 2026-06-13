package com.logismart.persistencia;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.singleton.ConexionBD;
import java.util.Optional;

/**
 * Data Mapper para Envio.
 * Desacopla el objeto de dominio de la lógica de acceso a datos.
 * Delega en ConexionBD (Singleton) para trazabilidad de queries.
 */
public class EnvioMapper {
    private final ConexionBD bd = ConexionBD.obtenerInstancia();

    public void insertar(Envio envio) {
        bd.ejecutarQuery("INSERT INTO envios VALUES ('" + envio.getId() + "', '"
                + envio.getOrigen() + "', '" + envio.getDestino() + "', '"
                + envio.getEstado() + "', " + envio.getPeso() + ", " + envio.getCosto() + ")");
    }

    public void actualizar(Envio envio) {
        bd.ejecutarQuery("UPDATE envios SET estado='" + envio.getEstado()
                + "', costo=" + envio.getCosto() + " WHERE id='" + envio.getId() + "'");
    }

    public void eliminar(String id) {
        bd.ejecutarQuery("DELETE FROM envios WHERE id='" + id + "'");
    }

    public Optional<Envio> buscarPorId(String id) {
        bd.ejecutarQuery("SELECT * FROM envios WHERE id='" + id + "'");
        return Optional.empty();
    }
}
