package com.logismart.dominio.envio.estado;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstadoEnvio;

public class EstadoEntregado implements EstadoEnvio {
    @Override public void validar(Envio e)  { System.out.println("[Estado] No se puede validar un envío entregado."); }
    @Override public void entregar(Envio e) { System.out.println("[Estado] El envío ya fue entregado."); }
    @Override public void cancelar(Envio e) { System.out.println("[Estado] No se puede cancelar un envío entregado."); }
    @Override public void retener(Envio e)  { System.out.println("[Estado] No se puede retener un envío entregado."); }
    @Override public void devolver(Envio e) { e.cambiarEstado(new EstadoConfirmado()); }
    @Override public void reclamar(Envio e) { System.out.println("[Estado] Reclamo registrado para envío entregado."); }
    @Override public String obtenerNombre() { return "ENTREGADO"; }
}
