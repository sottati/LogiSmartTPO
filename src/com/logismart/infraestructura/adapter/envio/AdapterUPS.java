package com.logismart.infraestructura.adapter.envio;

import com.logismart.dominio.envio.Envio;

public class AdapterUPS implements ProveedorEnvio {
    private final UPSConnector upsConnector = new UPSConnector();

    @Override
    public boolean crearEnvio(Envio envio) {
        boolean resultado = upsConnector.sendPackage(envio.getOrigen(), envio.getDestino(), envio.getPeso());
        if (resultado) {
            envio.getSeguimiento().actualizarEstado("UPS:" + System.currentTimeMillis());
        }
        return resultado;
    }

    @Override
    public String obtenerEstado(String numeroSeguimiento) {
        return upsConnector.trackPackage(numeroSeguimiento);
    }

    @Override
    public double calcularCosto(Envio envio) {
        return upsConnector.estimateCost(envio.getOrigen(), envio.getDestino(), envio.getPeso());
    }

    @Override
    public String obtenerNombre() {
        return "UPS";
    }
}

