package com.logismart.infraestructura.comportamiento.chain;

public class ValidadorPago extends ValidadorEnvio {

    @Override
    public boolean validar(ContextoValidacion ctx) {
        System.out.println("[" + obtenerNombre() + "] Verificando pago...");
        var cobro = ctx.getCobro();

        if (ctx.getEnvio().getCosto() <= 0) {
            System.err.println("  ✗ Costo inválido");
            return false;
        }
        if (cobro == null || cobro.getMedioPago() == null || cobro.getMedioPago().isEmpty()) {
            System.err.println("  ✗ Método de pago no especificado");
            return false;
        }

        System.out.println("  ✓ Pago válido");
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override
    public String obtenerNombre() { return "ValidadorPago"; }
}

