package com.logismart.infraestructura.adapter.envio;

import com.logismart.dominio.envio.Envio;

public interface ProveedorEnvio {
    String  crearEnvio(Envio envio);
    String  obtenerEstado(String numeroSeguimiento);
    double  calcularCosto(Envio envio);
    String  obtenerNombre();
}
