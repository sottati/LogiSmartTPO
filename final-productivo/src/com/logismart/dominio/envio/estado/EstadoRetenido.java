package com.logismart.dominio.envio.estado;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstadoEnvio;

public class EstadoRetenido implements EstadoEnvio {
    @Override public void validar(Envio e)  { e.cambiarEstado(new EstadoEnTransito()); }
    @Override public void entregar(Envio e) { throw new IllegalStateException("No se puede entregar un envío retenido."); }
    @Override public void cancelar(Envio e) { e.cambiarEstado(new EstadoCancelado()); }
    @Override public void retener(Envio e)  { throw new IllegalStateException("El envío ya está retenido."); }
    @Override public void devolver(Envio e) { e.cambiarEstado(new EstadoConfirmado()); }
    @Override public void reclamar(Envio e) { /* reclamo válido en RETENIDO — sin cambio de estado */ }
    @Override public String obtenerNombre() { return "RETENIDO"; }
}
