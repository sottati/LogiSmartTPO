package com.logismart.infraestructura.flyweight.ubicacion;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FabricaUbicaciones {
    private static final Map<String, Ubicacion> UBICACIONES = new LinkedHashMap<>();

    private FabricaUbicaciones() {
    }

    public static Ubicacion obtener(String ciudad, String provincia, String codigoPostal) {
        String clave = ciudad + "|" + provincia + "|" + codigoPostal;
        if (!UBICACIONES.containsKey(clave)) {
            Ubicacion nueva = new Ubicacion(ciudad, provincia, codigoPostal);
            UBICACIONES.put(clave, nueva);
            System.out.println("[Flyweight] Nueva ubicacion creada: " + clave);
        }
        return UBICACIONES.get(clave);
    }

    public static void mostrarEstadisticas() {
        System.out.println("\n=== Estadisticas de Flyweight ===");
        System.out.println("Ubicaciones unicas en memoria: " + UBICACIONES.size());
        System.out.println("Ubicaciones almacenadas:");
        for (String clave : UBICACIONES.keySet()) {
            System.out.println(" - " + clave);
        }
    }

    public static int totalUbicaciones() {
        return UBICACIONES.size();
    }

    public static void limpiar() {
        UBICACIONES.clear();
    }
}
