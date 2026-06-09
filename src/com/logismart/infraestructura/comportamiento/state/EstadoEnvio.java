package com.logismart.infraestructura.comportamiento.state;

import com.logismart.dominio.envio.Envio;

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
            case "EN_CURSO":    return new EstadoEnCurso();
            case "CERRADO":     return new EstadoCerrado();
            default:            return new EstadoPendiente();
        }
    }
}

