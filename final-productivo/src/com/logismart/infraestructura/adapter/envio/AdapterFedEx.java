package com.logismart.infraestructura.adapter.envio;

import com.logismart.dominio.envio.Envio;

public class AdapterFedEx implements ProveedorEnvio {
    private final FedExAPI api = new FedExAPI();

    @Override
    public String crearEnvio(Envio envio) {
        return api.shipPackage(envio.getOrigen(), envio.getDestino(), envio.getPeso());
    }

    @Override
    public String obtenerEstado(String numeroSeguimiento) {
        return api.getTrackingStatus(numeroSeguimiento);
    }

    @Override
    public double calcularCosto(Envio envio) {
        return api.getRateQuote(envio.getPeso());
    }

    @Override
    public String obtenerNombre() { return "FedEx"; }
}
