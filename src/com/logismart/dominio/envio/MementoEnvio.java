package com.logismart.dominio.envio;

/**
 * Snapshot inmutable del estado de un Envío en un momento dado.
 *
 * Vive en el paquete dominio porque captura datos puramente de dominio
 * (estado del ciclo de vida, atributos logísticos). No es un mecanismo
 * técnico genérico: es una fotografía de una entidad de negocio concreta.
 *
 * Trade-off documentado: podría vivir en infraestructura.comportamiento.memento
 * para mayor cohesión del patrón, pero eso crearía una dependencia
 * dominio→infraestructura al ser usada por Envio como Originador.
 * Se prefiere mantener el dominio autónomo (Clean Architecture).
 *
 * Patrón: Memento (GoF) - Hito 11
 */
public class MementoEnvio {

    private final String estado;
    private final String origen;
    private final String destino;
    private final double peso;
    private final double costo;
    private final long   timestamp;

    public MementoEnvio(String estado, String origen, String destino,
                        double peso, double costo) {
        this.estado    = estado;
        this.origen    = origen;
        this.destino   = destino;
        this.peso      = peso;
        this.costo     = costo;
        this.timestamp = System.currentTimeMillis();
    }

    public String obtenerEstado()    { return estado;    }
    public String obtenerOrigen()    { return origen;    }
    public String obtenerDestino()   { return destino;   }
    public double obtenerPeso()      { return peso;      }
    public double obtenerCosto()     { return costo;     }
    public long   obtenerTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "Memento{estado='" + estado + "', origen='" + origen
             + "', destino='" + destino + "', peso=" + peso
             + ", costo=" + costo + "}";
    }
}

