package com.logismart.infraestructura.singleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class Logger {
    private static volatile Logger instancia;
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final Path   archivo = Path.of("logismart.log");
    private final Object lock    = new Object();

    private Logger() {}

    public static Logger obtenerInstancia() {
        if (instancia == null) {
            synchronized (Logger.class) {
                if (instancia == null) instancia = new Logger();
            }
        }
        return instancia;
    }

    public void log(String mensaje) {
        if (mensaje == null || mensaje.isBlank()) return;
        escribir(String.format("[%s] %s%n", LocalDateTime.now().format(FMT), mensaje));
    }

    public void logError(String contexto, Exception e) {
        log("ERROR " + contexto + " → " + (e == null ? "sin detalle" : e.getMessage()));
    }

    private void escribir(String linea) {
        synchronized (lock) {
            try {
                Files.writeString(archivo, linea,
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            } catch (IOException ex) {
                System.err.println("[Logger] No se pudo escribir: " + ex.getMessage());
            }
        }
    }
}
