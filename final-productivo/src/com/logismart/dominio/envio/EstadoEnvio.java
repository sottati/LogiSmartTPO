package com.logismart.dominio.envio;

import com.logismart.dominio.envio.estado.EstadoCancelado;
import com.logismart.dominio.envio.estado.EstadoConfirmado;
import com.logismart.dominio.envio.estado.EstadoEnReparto;
import com.logismart.dominio.envio.estado.EstadoEnTransito;
import com.logismart.dominio.envio.estado.EstadoEntregado;
import com.logismart.dominio.envio.estado.EstadoRetenido;

public interface EstadoEnvio {
    void validar(Envio envio);
    void entregar(Envio envio);
    void cancelar(Envio envio);
    void retener(Envio envio);
    void devolver(Envio envio);
    void reclamar(Envio envio);
    String obtenerNombre();

    static EstadoEnvio fromNombre(String nombre) {
        switch (nombre) {
            case "CONFIRMADO":  return new EstadoConfirmado();
            case "EN_TRANSITO": return new EstadoEnTransito();
            case "EN_REPARTO":  return new EstadoEnReparto();
            case "RETENIDO":    return new EstadoRetenido();
            case "ENTREGADO":   return new EstadoEntregado();
            case "CANCELADO":   return new EstadoCancelado();
            default:            return new EstadoConfirmado();
        }
    }
}
