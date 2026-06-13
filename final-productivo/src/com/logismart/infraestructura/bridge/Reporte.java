package com.logismart.infraestructura.bridge;

public abstract class Reporte {
    protected GeneradorReporte generador;

    protected Reporte(GeneradorReporte generador) {
        this.generador = generador;
    }

    public abstract String generarContenido();

    public String generar() {
        return generador.formatear(generarContenido());
    }

    public void setGenerador(GeneradorReporte generador) { this.generador = generador; }
    public String obtenerNombreArchivo(String nombre)    { return nombre + "." + generador.obtenerExtension(); }
}
