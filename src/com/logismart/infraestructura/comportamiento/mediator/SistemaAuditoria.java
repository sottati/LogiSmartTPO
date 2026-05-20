package com.logismart.infraestructura.comportamiento.mediator;

import com.logismart.dominio.Envio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Componente Mediator - Sistema de Auditoría.
 * Registra cada evento del pipeline para trazabilidad completa.
 * No necesita mediador: sólo recibe registros, no dispara eventos.
 *
 * Patrón: Mediator (GoF) - Hito 11
 */
public class SistemaAuditoria {

    private final List<String> logs = new ArrayList<>();

    public void registrar(String evento, Object datos) {
        String id = (datos instanceof Envio) ? ((Envio) datos).getId() : datos.toString();
        String entrada = "[Auditoria] " + evento + " - envio=" + id;
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
