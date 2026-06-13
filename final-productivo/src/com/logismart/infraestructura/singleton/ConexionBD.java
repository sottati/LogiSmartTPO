package com.logismart.infraestructura.singleton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class ConexionBD {
    private static volatile ConexionBD instancia;
    private final List<String> historial = new CopyOnWriteArrayList<>();

    private ConexionBD() {}

    public static ConexionBD obtenerInstancia() {
        if (instancia == null) {
            synchronized (ConexionBD.class) {
                if (instancia == null) instancia = new ConexionBD();
            }
        }
        return instancia;
    }

    public void ejecutarQuery(String sql) {
        if (sql == null || sql.isBlank()) throw new IllegalArgumentException("Query vacía");
        historial.add("[" + LocalDateTime.now() + "] " + sql);
    }

    public List<String> obtenerHistorial() { return List.copyOf(historial); }
    public int          cantidadQueries()  { return historial.size(); }
}
