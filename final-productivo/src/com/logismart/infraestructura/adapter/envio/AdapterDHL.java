package com.logismart.infraestructura.adapter.envio;

import com.logismart.dominio.envio.Envio;

public class AdapterDHL implements ProveedorEnvio {
    private final DHLAPI api = new DHLAPI();

    @Override
    public String crearEnvio(Envio envio) {
        return api.registrarPaquete(envio.getOrigen(), envio.getDestino(), envio.getPeso());
    }

    @Override
    public String obtenerEstado(String numeroSeguimiento) {
        return api.consultarEstado(numeroSeguimiento);
    }

    @Override
    public double calcularCosto(Envio envio) {
        return api.calcularTarifa(envio.getOrigen(), envio.getDestino(), envio.getPeso());
    }

    @Override
    public String obtenerNombre() { return "DHL"; }
}
