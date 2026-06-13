package com.logismart.infraestructura.adapter.pago;

public class PayPalAPI {
    public String crearTransaccion(double amount, String reference) {
        return "PP-TXN-" + System.currentTimeMillis();
    }
    public String consultarTransaccion(String txnId) { return "COMPLETED"; }
    public void revertirTransaccion(String txnId) {}
}
