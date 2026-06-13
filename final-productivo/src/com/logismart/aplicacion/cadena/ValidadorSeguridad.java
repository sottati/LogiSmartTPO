package com.logismart.aplicacion.cadena;

public class ValidadorSeguridad extends ValidadorEnvio {
    @Override
    public boolean validar(ContextoValidacion ctx) {
        if (ctx.getEnvio().getDestino().contains("Restringido")) {
            System.out.println("[" + obtenerNombre() + "] Fallo: destino restringido");
            return false;
        }
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override public String obtenerNombre() { return "ValidadorSeguridad"; }
}
