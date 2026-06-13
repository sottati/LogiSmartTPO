package com.logismart.aplicacion.cadena;

/**
 * Construye y ejecuta la cadena de validación en orden fijo:
 * Datos → Inventario → Pago → Seguridad → Capacidad.
 * Falla rápido: el primer validador que no pasa corta la cadena.
 */
public class CadenaValidadores {
    private final ValidadorEnvio cabeza;

    public CadenaValidadores(SistemaInventario inventario, SistemaCapacidad capacidad) {
        ValidadorEnvio datos      = new ValidadorDatos();
        ValidadorEnvio inv        = new ValidadorInventario(inventario);
        ValidadorEnvio pago       = new ValidadorPago();
        ValidadorEnvio seguridad  = new ValidadorSeguridad();
        ValidadorEnvio cap        = new ValidadorCapacidad(capacidad);

        datos.setSiguiente(inv);
        inv.setSiguiente(pago);
        pago.setSiguiente(seguridad);
        seguridad.setSiguiente(cap);

        this.cabeza = datos;
    }

    public boolean validar(ContextoValidacion ctx) {
        return cabeza.validar(ctx);
    }
}
