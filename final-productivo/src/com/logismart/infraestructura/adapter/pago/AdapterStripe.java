package com.logismart.infraestructura.adapter.pago;

public class AdapterStripe implements ProveedorPago {
    private final StripeAPI api = new StripeAPI();

    @Override
    public String procesarPago(double monto, String referencia) {
        long centavos = Math.round(monto * 100);
        return api.charge(centavos, "USD", referencia);
    }

    @Override
    public String obtenerEstado(String idTransaccion) {
        return api.retrieveCharge(idTransaccion);
    }

    @Override
    public void reembolsar(String idTransaccion, double monto) {
        api.refund(idTransaccion, Math.round(monto * 100));
    }

    @Override
    public String obtenerNombre() { return "Stripe"; }
}
