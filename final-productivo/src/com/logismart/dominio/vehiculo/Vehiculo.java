package com.logismart.dominio.vehiculo;

public class Vehiculo {

    private String  id;
    private String  patente;
    private double  capacidadKg;
    private String  tipo;
    private boolean disponible;

    public Vehiculo(String id, String patente, double capacidadKg,
                    String tipo, boolean disponible) {
        this.id          = id;
        this.patente     = patente;
        this.capacidadKg = capacidadKg;
        this.tipo        = tipo;
        this.disponible  = disponible;
    }

    public boolean puedeCargar(double pesoKg) {
        return disponible && (pesoKg <= capacidadKg * 0.90);
    }

    public boolean estaOperativo() { return disponible; }

    public void asignarRuta() { disponible = false; }
    public void liberar()     { disponible = true;  }

    public void actualizarCapacidad(double nuevaCapacidadKg) {
        this.capacidadKg = nuevaCapacidadKg;
    }

    public double getCostoBaseKm() { return 1.0; }

    public String  getId()          { return id; }
    public String  getPatente()     { return patente; }
    public double  getCapacidadKg() { return capacidadKg; }
    public String  getTipo()        { return tipo; }
    public boolean isDisponible()   { return disponible; }

    @Override
    public String toString() {
        return tipo + "{id='" + id + "', patente='" + patente
             + "', cap=" + capacidadKg + "kg}";
    }
}
