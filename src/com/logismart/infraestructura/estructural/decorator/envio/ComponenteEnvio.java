package com.logismart.infraestructura.estructural.decorator.envio;

public interface ComponenteEnvio {
    double obtenerCosto();
    int obtenerTiempoEntrega();
    String obtenerDescripcion();
    String obtenerServicios();
}
