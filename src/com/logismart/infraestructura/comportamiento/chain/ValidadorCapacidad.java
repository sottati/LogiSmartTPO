package com.logismart.infraestructura.comportamiento.chain;

public class ValidadorCapacidad extends ValidadorEnvio {
    private final SistemaCapacidad sistemaCapacidad;

    public ValidadorCapacidad(SistemaCapacidad sistemaCapacidad) {
        this.sistemaCapacidad = sistemaCapacidad;
    }

    @Override
    public boolean validar(ContextoValidacion ctx) {
        System.out.println("[" + obtenerNombre() + "] Verificando capacidad...");

        if (!sistemaCapacidad.hayEspacioDisponible(ctx.getEnvio().getPeso())) {
            System.err.println("  ✗ No hay espacio disponible");
            return false;
        }

        System.out.println("  ✓ Capacidad disponible");
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override
    public String obtenerNombre() { return "ValidadorCapacidad"; }
}

