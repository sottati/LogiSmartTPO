package com.logismart.infraestructura.abstractfactory;

public class GoogleMapsArgentina implements ProveedorMapas {

    @Override
    public String obtenerRuta(String origen, String destino) {
        String ruta = "Ruta AR: " + origen + " → " + destino;
        System.out.println("[GoogleMaps AR] " + ruta);
        return ruta;
    }
}
