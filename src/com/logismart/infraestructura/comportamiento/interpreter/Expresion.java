package com.logismart.infraestructura.comportamiento.interpreter;

import com.logismart.dominio.envio.Envio;

public interface Expresion {
    boolean evaluar(Envio envio);
}

