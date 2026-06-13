package com.logismart.infraestructura.adapter.envio;

import com.logismart.dominio.envio.Envio;

public class AdapterUPS implements ProveedorEnvio {
    private final UPSAPI api = new UPSAPI();
    private static final double KG_TO_LBS = 2.20462;

    @Override
    public String crearEnvio(Envio envio) {
        return api.createShipment(envio.getPeso() * KG_TO_LBS);
    }

    @Override
    public String obtenerEstado(String numeroSeguimiento) {
        return api.trackShipment(numeroSeguimiento);
    }

    @Override
    public double calcularCosto(Envio envio) {
        return api.calculateRate(envio.getPeso() * KG_TO_LBS);
    }

    @Override
    public String obtenerNombre() { return "UPS"; }
}
