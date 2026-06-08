package com.logismart.infraestructura.comportamiento.chain;

public class ValidadorSeguridad extends ValidadorEnvio {

    @Override
    public boolean validar(ContextoValidacion ctx) {
        System.out.println("[" + obtenerNombre() + "] Verificando restricciones...");

        if (ctx.getEnvio().getDestino().contains("Restringido")) {
            System.err.println("  ✗ Destino restringido");
            return false;
        }

        System.out.println("  ✓ Seguridad OK");
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override
    public String obtenerNombre() { return "ValidadorSeguridad"; }
}

