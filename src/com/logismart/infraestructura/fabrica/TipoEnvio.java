package com.logismart.infraestructura.fabrica;

public enum TipoEnvio {
    EXPRESS("ALTA", 1, 1.45),
    STANDARD("MEDIA", 3, 1.00),
    ECONOMICO("BAJA", 7, 0.85);

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
