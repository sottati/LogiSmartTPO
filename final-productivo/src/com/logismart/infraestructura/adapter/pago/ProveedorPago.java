package com.logismart.infraestructura.adapter.pago;

public interface ProveedorPago {
    String  procesarPago(double monto, String referencia);
    String  obtenerEstado(String idTransaccion);
    void    reembolsar(String idTransaccion, double monto);
    String  obtenerNombre();
}
