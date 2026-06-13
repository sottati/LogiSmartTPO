package com.logismart.aplicacion.cadena;

public class ValidadorCapacidad extends ValidadorEnvio {
    private final SistemaCapacidad sistemaCapacidad;

    public ValidadorCapacidad(SistemaCapacidad sistemaCapacidad) {
        this.sistemaCapacidad = sistemaCapacidad;
    }

    @Override
    public boolean validar(ContextoValidacion ctx) {
        if (!sistemaCapacidad.hayEspacioDisponible(ctx.getEnvio().getPeso())) {
            System.out.println("[" + obtenerNombre() + "] Fallo: sin espacio disponible");
            return false;
        }
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override public String obtenerNombre() { return "ValidadorCapacidad"; }
}
