package com.logismart.infraestructura.adapter.envio;

import com.logismart.dominio.envio.Envio;

public class AdapterDHL implements ProveedorEnvio {
    private final DHLAPI dhlAPI = new DHLAPI();

    @Override
    public boolean crearEnvio(Envio envio) {
        String codigo = dhlAPI.registrarPaquete(envio.getOrigen(), envio.getDestino(), envio.getPeso());
        envio.getSeguimiento().actualizarEstado("DHL:" + codigo);
        return true;
    }

    @Override
    public String obtenerEstado(String numeroSeguimiento) {
        return dhlAPI.consultarEstadoPaquete(numeroSeguimiento);
    }

    @Override
    public double calcularCosto(Envio envio) {
        return dhlAPI.calcularTarifa(envio.getOrigen(), envio.getDestino(), envio.getPeso());
    }

    @Override
    public String obtenerNombre() {
        return "DHL";
    }
}

