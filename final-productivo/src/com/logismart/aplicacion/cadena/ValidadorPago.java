package com.logismart.aplicacion.cadena;

public class ValidadorPago extends ValidadorEnvio {
    @Override
    public boolean validar(ContextoValidacion ctx) {
        if (ctx.getEnvio().getCosto() <= 0) {
            System.out.println("[" + obtenerNombre() + "] Fallo: costo inválido");
            return false;
        }
        var cobro = ctx.getCobro();
        if (cobro == null || cobro.getMedioPago() == null || cobro.getMedioPago().isBlank()) {
            System.out.println("[" + obtenerNombre() + "] Fallo: medio de pago no especificado");
            return false;
        }
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override public String obtenerNombre() { return "ValidadorPago"; }
}
