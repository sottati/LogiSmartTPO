package com.logismart.aplicacion.cadena;

import java.util.HashMap;
import java.util.Map;

public class SistemaInventario {
    private final Map<String, Integer> stock = new HashMap<>();

    public SistemaInventario() {
        stock.put("PROD-001", 10);
        stock.put("PROD-002", 5);
        stock.put("PROD-003", 3);
    }

    public boolean verificarStock(String productoId) {
        return productoId != null && stock.getOrDefault(productoId, 0) > 0;
    }

    public void reducirStock(String productoId) {
        stock.computeIfPresent(productoId, (k, v) -> v > 0 ? v - 1 : 0);
    }

    public void agregarStock(String productoId, int cantidad) {
        stock.merge(productoId, cantidad, Integer::sum);
    }
}
