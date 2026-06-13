package com.logismart.aplicacion.cadena;

public class ValidadorInventario extends ValidadorEnvio {
    private final SistemaInventario inventario;

    public ValidadorInventario(SistemaInventario inventario) {
        this.inventario = inventario;
    }

    @Override
    public boolean validar(ContextoValidacion ctx) {
        if (!inventario.verificarStock("PROD-001")) {
            System.out.println("[" + obtenerNombre() + "] Fallo: stock insuficiente");
            return false;
        }
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override public String obtenerNombre() { return "ValidadorInventario"; }
}
