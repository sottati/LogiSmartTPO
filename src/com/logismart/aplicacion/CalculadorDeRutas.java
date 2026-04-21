package com.logismart.aplicacion;

import com.logismart.dominio.Envio;
import com.logismart.dominio.Ruta;
import com.logismart.infraestructura.ruta.CalculadorDeRuta;

import java.util.List;

/**
 * GRASP Pure Fabrication: Servicio artificial que orquesta el calculo de la ruta optima.
 * Actua como fachada sobre las distintas estrategias de CalculadorDeRuta (Indirection).
 * Separa la logica de seleccion del Controller, que solo le pide la mejor ruta.
 */
public class CalculadorDeRutas {

    private CalculadorDeRuta estrategia;

    public CalculadorDeRutas(CalculadorDeRuta estrategia) {
        if (estrategia == null) {
            throw new IllegalArgumentException("La estrategia de calculo no puede ser nula");
        }
        this.estrategia = estrategia;
    }

    /**
     * Selecciona la mejor ruta para el envio segun la estrategia configurada.
     */
    public Ruta seleccionarRuta(Envio envio, List<Ruta> rutasDisponibles) {
        if (envio == null) {
            throw new IllegalArgumentException("El envio no puede ser nulo");
        }
        if (rutasDisponibles == null || rutasDisponibles.isEmpty()) {
            throw new IllegalStateException("No hay rutas disponibles para asignar al envio " + envio.getId());
        }
        return estrategia.calcular(envio, rutasDisponibles);
    }

    /**
     * Permite cambiar la estrategia de calculo en tiempo de ejecucion.
     * Protected Variations: el punto de variacion esta encapsulado aqui.
     */
    public void cambiarEstrategia(CalculadorDeRuta nuevaEstrategia) {
        if (nuevaEstrategia == null) {
            throw new IllegalArgumentException("La nueva estrategia no puede ser nula");
        }
        this.estrategia = nuevaEstrategia;
    }

    public String getEstrategiaActual() {
        return estrategia.getDescripcion();
    }
}
