package com.logismart.infraestructura.singleton;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton thread-safe para trazas del sistema.
 */
public final class Logger {

    private static volatile Logger instancia;
    private static final DateTimeFormatter TS_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path archivoLog;
    private final Object lock;

    private Logger() {
        this.archivoLog = Path.of("logismart.log");
        this.lock = new Object();
    }

    public static Logger obtenerInstancia() {
        if (instancia == null) {
            synchronized (Logger.class) {
                if (instancia == null) {
                    instancia = new Logger();
                }
            }
        }
        return instancia;
    }

    public void log(String mensaje) {
        if (mensaje == null || mensaje.isBlank()) {
            return;
        }
        escribir(String.format("[%s] %s%n", LocalDateTime.now().format(TS_FORMAT), mensaje));
    }

    public void logError(String contexto, Exception error) {
        String detalle = (error == null) ? "sin detalle" : error.getMessage();
        log("ERROR " + contexto + " -> " + detalle);
    }

    public Path getArchivoLog() {
        return archivoLog;
    }

    private void escribir(String linea) {
        synchronized (lock) {
            try {
                Files.writeString(
                        archivoLog,
                        linea,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.WRITE,
                        StandardOpenOption.APPEND
                );
            } catch (IOException e) {
                throw new IllegalStateException("No se pudo escribir el log", e);
            }
        }
    }
}
