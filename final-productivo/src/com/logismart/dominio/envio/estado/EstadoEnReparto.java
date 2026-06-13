package com.logismart.dominio.envio.estado;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstadoEnvio;

public class EstadoEnReparto implements EstadoEnvio {
    @Override public void validar(Envio e)  { System.out.println("[Estado] El envío ya fue validado."); }
    @Override public void entregar(Envio e) { e.cambiarEstado(new EstadoEntregado()); }
    @Override public void cancelar(Envio e) { System.out.println("[Estado] No se puede cancelar un envío en reparto."); }
    @Override public void retener(Envio e)  { e.cambiarEstado(new EstadoRetenido()); }
    @Override public void devolver(Envio e) { e.cambiarEstado(new EstadoEnTransito()); }
    @Override public void reclamar(Envio e) { System.out.println("[Estado] Reclamo registrado para envío en reparto."); }
    @Override public String obtenerNombre() { return "EN_REPARTO"; }
}
