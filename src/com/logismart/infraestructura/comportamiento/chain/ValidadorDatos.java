package com.logismart.infraestructura.comportamiento.chain;

public class ValidadorDatos extends ValidadorEnvio {

    @Override
    public boolean validar(ContextoValidacion ctx) {
        System.out.println("[" + obtenerNombre() + "] Validando...");
        var envio = ctx.getEnvio();

        if (envio.getOrigen() == null || envio.getOrigen().isEmpty()) {
            System.err.println("  ✗ Origen inválido");
            return false;
        }
        if (envio.getDestino() == null || envio.getDestino().isEmpty()) {
            System.err.println("  ✗ Destino inválido");
            return false;
        }
        if (envio.getPeso() <= 0) {
            System.err.println("  ✗ Peso inválido");
            return false;
        }

        System.out.println("  ✓ Datos válidos");
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override
    public String obtenerNombre() { return "ValidadorDatos"; }
}

