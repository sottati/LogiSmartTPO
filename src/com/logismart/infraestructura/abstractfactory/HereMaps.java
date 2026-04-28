package com.logismart.infraestructura.abstractfactory;

public class HereMaps implements ProveedorMapas {

    @Override
    public String obtenerRuta(String origen, String destino) {
        String ruta = "Ruta BR: " + origen + " → " + destino;
        System.out.println("[HereMaps BR] " + ruta);
        return ruta;
    }
}
