package com.logismart.infraestructura.ruta;

import com.logismart.dominio.Envio;
import com.logismart.dominio.Ruta;

import java.util.List;

/**
 * GRASP Polymorphism: Estrategia de seleccion de ruta por menor distancia total.
 * Reemplaza el condicional: if (criterio.equals("CERCANA")) { ... }
 */
public class RutaMasCercana implements CalculadorDeRuta {

    @Override
    public Ruta calcular(Envio envio, List<Ruta> rutas) {
        if (rutas == null || rutas.isEmpty()) {
            throw new IllegalArgumentException("No hay rutas disponibles para el envio " + envio.getId());
        }
        return rutas.stream()
                .min((r1, r2) -> Double.compare(r1.calcularDistanciaTotal(), r2.calcularDistanciaTotal()))
                .orElseThrow(() -> new IllegalStateException("No se pudo seleccionar ruta"));
    }

    @Override
    public String getDescripcion() {
        return "Ruta de menor distancia total (km)";
    }
}
