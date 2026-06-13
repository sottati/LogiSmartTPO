package com.logismart.aplicacion.cadena;

import com.logismart.dominio.empresa.Cobro;
import com.logismart.dominio.envio.Envio;

public class ContextoValidacion {
    private final Envio envio;
    private final Cobro cobro;

    public ContextoValidacion(Envio envio, Cobro cobro) {
        this.envio = envio;
        this.cobro = cobro;
    }

    public Envio getEnvio() { return envio; }
    public Cobro getCobro() { return cobro; }
}
