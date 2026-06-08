package com.logismart.infraestructura.estructural.abstractfactory;

/** Producto abstracto - provee rutas según el proveedor regional. */
public interface ProveedorMapas {
    String obtenerRuta(String origen, String destino);
}
