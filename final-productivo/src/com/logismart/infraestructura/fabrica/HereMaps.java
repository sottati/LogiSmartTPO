package com.logismart.infraestructura.fabrica;

public class HereMaps implements ProveedorMapas {
    @Override
    public double calcularDistancia(String origen, String destino) {
        return 280.0; // stub: distancia promedio en Brasil
    }

    @Override
    public String obtenerNombre() { return "HERE Maps BR"; }
}
