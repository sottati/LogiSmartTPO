package com.logismart.infraestructura.comportamiento.strategy;

import com.logismart.dominio.Envio;

public interface EstrategiaCalculoCosto {
    double calcular(Envio envio);
    String obtenerNombre();
}
