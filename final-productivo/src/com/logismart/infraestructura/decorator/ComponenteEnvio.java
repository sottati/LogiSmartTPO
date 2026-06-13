package com.logismart.infraestructura.decorator;

public interface ComponenteEnvio {
    double obtenerCosto();
    int    obtenerTiempoEntrega();
    String obtenerDescripcion();
    String obtenerServicios();
}
