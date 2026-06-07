package com.logismart.infraestructura.comportamiento.mediator;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.ObservadorEnvio;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sistema de Auditoría unificado.
 * Funciona como componente Mediator (recibe registrar()) y como Observer (implementa actualizar()).
 * Un único objeto cubre ambos roles: auditoría de pipeline y auditoría de cambios de estado.
 *
 * Patrón: Mediator + Observer (GoF) - Hito 11
 */
public class SistemaAuditoria implements ObservadorEnvio {

    private final List<String> logs = new ArrayList<>();

    @Override
    public void actualizar(Envio envio) {
        registrar("CAMBIO_ESTADO", envio);
    }

    public void registrar(String evento, Object datos) {
        String entrada = "[Auditoria] " + evento + " - datos=" + datos;
        logs.add(entrada);
        System.out.println(entrada);
    }

    public void mostrarLogs() {
        System.out.println("\n=== Logs de Auditoría (" + logs.size() + " entradas) ===");
        for (String log : logs) System.out.println("  " + log);
    }

    public List<String> obtenerLogs() {
        return Collections.unmodifiableList(logs);
    }

    public int contarEventos(String evento) {
        return (int) logs.stream().filter(l -> l.contains(evento)).count();
    }
}

