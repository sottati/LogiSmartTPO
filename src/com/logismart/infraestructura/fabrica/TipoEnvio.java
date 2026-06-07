package com.logismart.infraestructura.fabrica;

import com.logismart.dominio.empresa.Empresa;
import com.logismart.dominio.envio.Envio;

import java.time.LocalDateTime;

public enum TipoEnvio {
    EXPRESS("ALTA", 1, 1.45) {
        @Override
        public Envio crearEnvio(String id, Empresa empresa, LocalDateTime fechaProgramada) {
            return new EnvioExpress(id, empresa, fechaProgramada);
        }
    },
    STANDARD("MEDIA", 3, 1.00) {
        @Override
        public Envio crearEnvio(String id, Empresa empresa, LocalDateTime fechaProgramada) {
            return new EnvioStandard(id, empresa, fechaProgramada);
        }
    },
    ECONOMICO("BAJA", 7, 0.85) {
        @Override
        public Envio crearEnvio(String id, Empresa empresa, LocalDateTime fechaProgramada) {
            return new EnvioEconomico(id, empresa, fechaProgramada);
        }
    };

    private final String prioridadAsociada;
    private final int diasEntregaEstimados;
    private final double multiplicadorCosto;

    TipoEnvio(String prioridadAsociada, int diasEntregaEstimados, double multiplicadorCosto) {
        this.prioridadAsociada = prioridadAsociada;
        this.diasEntregaEstimados = diasEntregaEstimados;
        this.multiplicadorCosto = multiplicadorCosto;
    }

    public String getPrioridadAsociada() {
        return prioridadAsociada;
    }

    public int getDiasEntregaEstimados() {
        return diasEntregaEstimados;
    }

    public double getMultiplicadorCosto() {
        return multiplicadorCosto;
    }

    public abstract Envio crearEnvio(String id, Empresa empresa, LocalDateTime fechaProgramada);

    public static TipoEnvio desdePrioridad(String prioridad) {
        if (prioridad == null || prioridad.isBlank()) {
            throw new IllegalArgumentException("La prioridad no puede ser vacia");
        }
        return switch (prioridad.toUpperCase()) {
            case "ALTA" -> EXPRESS;
            case "MEDIA" -> STANDARD;
            case "BAJA" -> ECONOMICO;
            default -> throw new IllegalArgumentException("Prioridad no soportada: " + prioridad);
        };
    }
}

