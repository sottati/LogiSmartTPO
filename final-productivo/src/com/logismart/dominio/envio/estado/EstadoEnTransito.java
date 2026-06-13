package com.logismart.dominio.envio.estado;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstadoEnvio;

public class EstadoEnTransito implements EstadoEnvio {
    @Override public void validar(Envio e)  { System.out.println("[Estado] El envío ya está en tránsito."); }
    @Override public void entregar(Envio e) { e.cambiarEstado(new EstadoEnReparto()); }
    @Override public void cancelar(Envio e) { e.cambiarEstado(new EstadoCancelado()); }
    @Override public void retener(Envio e)  { e.cambiarEstado(new EstadoRetenido()); }
    @Override public void devolver(Envio e) { e.cambiarEstado(new EstadoConfirmado()); }
    @Override public void reclamar(Envio e) { System.out.println("[Estado] Reclamo registrado para envío en tránsito."); }
    @Override public String obtenerNombre() { return "EN_TRANSITO"; }
}
