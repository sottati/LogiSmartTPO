package com.logismart.infraestructura.decorator.envio;

public interface Envio {
    double obtenerCosto();
    int obtenerTiempoEntrega();
    String obtenerDescripcion();
    String obtenerServicios();
}
