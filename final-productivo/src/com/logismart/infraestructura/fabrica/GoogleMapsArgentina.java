package com.logismart.infraestructura.fabrica;

public class GoogleMapsArgentina implements ProveedorMapas {
    @Override
    public double calcularDistancia(String origen, String destino) {
        return 350.0; // stub: distancia promedio en Argentina
    }

    @Override
    public String obtenerNombre() { return "Google Maps AR"; }
}
