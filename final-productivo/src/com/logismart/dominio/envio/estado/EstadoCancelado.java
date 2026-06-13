package com.logismart.dominio.envio.estado;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.envio.EstadoEnvio;

public class EstadoCancelado implements EstadoEnvio {
    @Override public void validar(Envio e)  { System.out.println("[Estado] No se puede validar un envío cancelado."); }
    @Override public void entregar(Envio e) { System.out.println("[Estado] No se puede entregar un envío cancelado."); }
    @Override public void cancelar(Envio e) { System.out.println("[Estado] El envío ya está cancelado."); }
    @Override public void retener(Envio e)  { System.out.println("[Estado] No se puede retener un envío cancelado."); }
    @Override public void devolver(Envio e) { System.out.println("[Estado] No se puede devolver un envío cancelado."); }
    @Override public void reclamar(Envio e) { System.out.println("[Estado] No se puede reclamar un envío cancelado."); }
    @Override public String obtenerNombre() { return "CANCELADO"; }
}
