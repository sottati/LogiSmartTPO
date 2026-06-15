package com.logismart.dominio.envio.estado;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstadoEnvio;

public class EstadoConfirmado implements EstadoEnvio {
    @Override public void validar(Envio e)  { e.cambiarEstado(new EstadoEnTransito()); }
    @Override public void entregar(Envio e) { throw new IllegalStateException("No se puede entregar un envío confirmado."); }
    @Override public void cancelar(Envio e) { e.cambiarEstado(new EstadoCancelado()); }
    @Override public void retener(Envio e)  { e.cambiarEstado(new EstadoRetenido()); }
    @Override public void devolver(Envio e) { throw new IllegalStateException("No se puede devolver un envío confirmado."); }
    @Override public void reclamar(Envio e) { throw new IllegalStateException("No se puede reclamar un envío confirmado."); }
    @Override public String obtenerNombre() { return "CONFIRMADO"; }
}
