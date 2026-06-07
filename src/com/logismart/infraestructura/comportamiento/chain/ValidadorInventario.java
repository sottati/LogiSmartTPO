package com.logismart.infraestructura.comportamiento.chain;

import com.logismart.dominio.envio.Orden;
import java.util.List;

public class ValidadorInventario extends ValidadorEnvio {
    private final SistemaInventario inventario;

    public ValidadorInventario(SistemaInventario inventario) {
        this.inventario = inventario;
    }

    @Override
    public boolean validar(ContextoValidacion ctx) {
        System.out.println("[" + obtenerNombre() + "] Verificando stock...");
        List<Orden> ordenes = ctx.getEnvio().getOrdenes();

        for (Orden orden : ordenes) {
            if (orden.getProductoId() != null && !inventario.verificarStock(orden.getProductoId())) {
                System.err.println("  ✗ Stock insuficiente para " + orden.getProductoId());
                return false;
            }
        }

        System.out.println("  ✓ Stock disponible");
        return siguiente == null || siguiente.validar(ctx);
    }

    @Override
    public String obtenerNombre() { return "ValidadorInventario"; }
}

