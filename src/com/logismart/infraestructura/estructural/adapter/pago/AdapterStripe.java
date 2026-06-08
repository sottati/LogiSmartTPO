package com.logismart.infraestructura.estructural.adapter.pago;

public class AdapterStripe implements ProveedorPago {
    private final StripeAPI stripeAPI = new StripeAPI();

    @Override
    public boolean procesarPago(double monto, String referencia) {
        return stripeAPI.charge(monto * 100, referencia);
    }

    @Override
    public String obtenerEstado(String idTransaccion) {
        String estado = stripeAPI.getChargeStatus(idTransaccion);
        return estado.equals("succeeded") ? "COMPLETADA" : "PENDIENTE";
    }

    @Override
    public void reembolsar(String idTransaccion, double monto) {
        System.out.println("Reembolso Stripe: " + idTransaccion + " $" + monto);
    }
}
