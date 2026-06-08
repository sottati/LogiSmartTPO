package com.logismart.infraestructura.comportamiento.interpreter;

import com.logismart.dominio.envio.Envio;

public class ExpresionRestringido implements Expresion {
    @Override
    public boolean evaluar(Envio envio) {
        return envio.getDestino() != null && envio.getDestino().contains("Restringido");
    }
}

