package com.logismart.infraestructura.singleton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Singleton thread-safe para centralizar acceso a capa de persistencia.
 * En este proyecto academico no abre una conexion real; registra consultas ejecutadas.
 */
public final class ConexionBD {

    private static volatile ConexionBD instancia;

    private final List<String> historialQueries;

    private ConexionBD() {
        this.historialQueries = new CopyOnWriteArrayList<>();
    }

    public static ConexionBD obtenerInstancia() {
        if (instancia == null) {
            synchronized (ConexionBD.class) {
                if (instancia == null) {
                    instancia = new ConexionBD();
                }
            }
        }
        return instancia;
    }

    public void ejecutarQuery(String sql) {
        if (sql == null || sql.isBlank()) {
            throw new IllegalArgumentException("La query no puede ser vacia");
        }
        String registro = String.format("[%s] %s", LocalDateTime.now(), sql);
        historialQueries.add(registro);
    }

    public List<String> obtenerHistorialQueries() {
        return List.copyOf(historialQueries);
    }
}
