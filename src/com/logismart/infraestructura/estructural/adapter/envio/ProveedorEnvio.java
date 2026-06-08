package com.logismart.infraestructura.estructural.adapter.envio;

import com.logismart.dominio.envio.Envio;

public interface ProveedorEnvio {
    boolean crearEnvio(Envio envio);
    String obtenerEstado(String numeroSeguimiento);
    double calcularCosto(Envio envio);
    String obtenerNombre();
}

