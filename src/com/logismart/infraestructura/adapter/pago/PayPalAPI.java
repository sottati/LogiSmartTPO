package com.logismart.infraestructura.adapter.pago;

public class PayPalAPI {
    public String crearTransaccion(double cantidad, String descripcion) {
        return "PP-" + System.currentTimeMillis();
    }

    public String consultarTransaccion(String id) {
        return "COMPLETADA";
    }
}
