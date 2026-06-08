package com.logismart.infraestructura.estructural.ruta;

import com.logismart.dominio.envio.Envio;
import com.logismart.dominio.ruta.Ruta;

import java.util.List;

/**
 * GRASP Polymorphism: Estrategia de seleccion de ruta por menor duracion estimada.
 * Modela la preferencia de minimizar tiempo en transito considerando congestion de trafico.
 * En produccion podria consultar una API de trafico en tiempo real.
 */
public class RutaMenosCongestionada implements CalculadorDeRuta {

    @Override
    public Ruta calcular(Envio envio, List<Ruta> rutas) {
        if (rutas == null || rutas.isEmpty()) {
            throw new IllegalArgumentException("No hay rutas disponibles para el envio " + envio.getId());
        }
        return rutas.stream()
                .min((r1, r2) -> Integer.compare(r1.getDuracionEstimadaMin(), r2.getDuracionEstimadaMin()))
                .orElseThrow(() -> new IllegalStateException("No se pudo seleccionar ruta"));
    }

    @Override
    public String getDescripcion() {
        return "Ruta de menor duracion estimada (tiempo de transito)";
    }
}

