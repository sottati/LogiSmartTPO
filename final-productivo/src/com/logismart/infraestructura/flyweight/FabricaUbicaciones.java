package com.logismart.infraestructura.flyweight;

import java.util.LinkedHashMap;
import java.util.Map;

public final class FabricaUbicaciones {
    private static final Map<String, Ubicacion> CACHE = new LinkedHashMap<>();

    private FabricaUbicaciones() {}

    public static Ubicacion obtener(String ciudad, String provincia, String codigoPostal) {
        String clave = ciudad + "|" + provincia + "|" + codigoPostal;
        return CACHE.computeIfAbsent(clave, k -> new Ubicacion(ciudad, provincia, codigoPostal));
    }

    public static int totalInstancias() { return CACHE.size(); }

    public static void limpiar() { CACHE.clear(); }
}
