package com.logismart.infraestructura.comportamiento.strategy;

import com.logismart.dominio.envio.Envio;

public interface EstrategiaCalculoCosto {
    double calcular(Envio envio);
    String obtenerNombre();
}

