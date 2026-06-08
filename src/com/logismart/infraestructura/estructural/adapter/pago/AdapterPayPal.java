package com.logismart.infraestructura.estructural.adapter.pago;

public class AdapterPayPal implements ProveedorPago {
    private final PayPalAPI paypalAPI = new PayPalAPI();

    @Override
    public boolean procesarPago(double monto, String referencia) {
        String id = paypalAPI.crearTransaccion(monto, referencia);
        return id != null;
    }

    @Override
    public String obtenerEstado(String idTransaccion) {
        return paypalAPI.consultarTransaccion(idTransaccion);
    }

    @Override
    public void reembolsar(String idTransaccion, double monto) {
        System.out.println("Reembolso PayPal: " + idTransaccion + " $" + monto);
    }
}
