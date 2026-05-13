package com.logismart.infraestructura.comportamiento.chain;

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
        if (productoId == null) return false;
        return stock.getOrDefault(productoId, 0) > 0;
    }

    public void reducirStock(String productoId) {
        stock.computeIfPresent(productoId, (k, v) -> v > 0 ? v - 1 : 0);
    }
}
