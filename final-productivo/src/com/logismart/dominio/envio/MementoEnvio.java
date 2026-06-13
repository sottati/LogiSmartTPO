package com.logismart.dominio.envio;

public class MementoEnvio {

    private final String estado;
    private final String origen;
    private final String destino;
    private final double peso;
    private final double costo;
    private final long   timestamp;

    public MementoEnvio(String estado, String origen, String destino, double peso, double costo) {
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
        return "Snapshot{estado='" + estado + "', origen='" + origen
             + "', destino='" + destino + "', peso=" + peso
             + ", costo=" + costo + "}";
    }
}
