package com.logismart.infraestructura.adapter.pago;

public class AdapterPayPal implements ProveedorPago {
    private final PayPalAPI api = new PayPalAPI();

    @Override
    public String procesarPago(double monto, String referencia) {
        return api.crearTransaccion(monto, referencia);
    }

    @Override
    public String obtenerEstado(String idTransaccion) {
        return api.consultarTransaccion(idTransaccion);
    }

    @Override
    public void reembolsar(String idTransaccion, double monto) {
        api.revertirTransaccion(idTransaccion);
    }

    @Override
    public String obtenerNombre() { return "PayPal"; }
}
