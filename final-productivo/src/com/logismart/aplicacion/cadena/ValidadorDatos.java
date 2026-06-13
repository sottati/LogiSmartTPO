package com.logismart.aplicacion.cadena;

public class ValidadorDatos extends ValidadorEnvio {
    @Override
    public boolean validar(ContextoValidacion ctx) {
        var e = ctx.getEnvio();
        if (e.getOrigen()  == null || e.getOrigen().isBlank())  return fallo("Origen inválido");
        if (e.getDestino() == null || e.getDestino().isBlank()) return fallo("Destino inválido");
        if (e.getPeso()    <= 0)                                 return fallo("Peso inválido");
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override public String obtenerNombre() { return "ValidadorDatos"; }

    private boolean fallo(String motivo) {
        System.out.println("[" + obtenerNombre() + "] Fallo: " + motivo);
        return false;
    }
}
