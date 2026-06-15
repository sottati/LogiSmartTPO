package com.logismart.dominio.envio.estado;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstadoEnvio;

public class EstadoEntregado implements EstadoEnvio {
    @Override public void validar(Envio e)  { throw new IllegalStateException("No se puede validar un envío entregado."); }
    @Override public void entregar(Envio e) { throw new IllegalStateException("El envío ya fue entregado."); }
    @Override public void cancelar(Envio e) { throw new IllegalStateException("No se puede cancelar un envío entregado."); }
    @Override public void retener(Envio e)  { throw new IllegalStateException("No se puede retener un envío entregado."); }
    @Override public void devolver(Envio e) { e.cambiarEstado(new EstadoConfirmado()); }
    @Override public void reclamar(Envio e) { /* reclamo válido en ENTREGADO — sin cambio de estado */ }
    @Override public String obtenerNombre() { return "ENTREGADO"; }
}
