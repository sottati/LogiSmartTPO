package com.logismart.infraestructura.ruta;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.ruta.Ruta;

import java.util.List;

/**
 * GRASP Polymorphism: Estrategia de seleccion de ruta por menor costo operativo estimado.
 * Delega el calculo de costo en Ruta.calcularCostoEstimado() (Expert del Hito 4).
 */
public class RutaMasBarata implements CalculadorDeRuta {

    @Override
    public Ruta calcular(Envio envio, List<Ruta> rutas) {
        if (rutas == null || rutas.isEmpty()) {
            throw new IllegalArgumentException("No hay rutas disponibles para el envio " + envio.getId());
        }
        return rutas.stream()
                .min((r1, r2) -> Double.compare(r1.calcularCostoEstimado(), r2.calcularCostoEstimado()))
                .orElseThrow(() -> new IllegalStateException("No se pudo seleccionar ruta"));
    }

    @Override
    public String getDescripcion() {
        return "Ruta de menor costo operativo estimado ($)";
    }
}

