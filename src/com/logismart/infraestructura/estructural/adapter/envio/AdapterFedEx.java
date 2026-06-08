package com.logismart.infraestructura.estructural.adapter.envio;

import com.logismart.dominio.envio.Envio;

public class AdapterFedEx implements ProveedorEnvio {
    private final FedExAPI fedexAPI = new FedExAPI();

    @Override
    public boolean crearEnvio(Envio envio) {
        int shipmentId = fedexAPI.crearShipment(envio.getOrigen(), envio.getDestino(), envio.getPeso());
        envio.getSeguimiento().actualizarEstado("FDX:" + shipmentId);
        return true;
    }

    @Override
    public String obtenerEstado(String numeroSeguimiento) {
        String id = numeroSeguimiento.startsWith("FDX-") ? numeroSeguimiento.substring(4) : numeroSeguimiento;
        return fedexAPI.getShipmentStatus(Integer.parseInt(id));
    }

    @Override
    public double calcularCosto(Envio envio) {
        return fedexAPI.getShippingRate(envio.getOrigen(), envio.getDestino(), envio.getPeso());
    }

    @Override
    public String obtenerNombre() {
        return "FedEx";
    }
}

