package com.logismart.infraestructura.decorator.envio;

public interface ComponenteEnvio {
    double obtenerCosto();
    int obtenerTiempoEntrega();
    String obtenerDescripcion();
    String obtenerServicios();
}
