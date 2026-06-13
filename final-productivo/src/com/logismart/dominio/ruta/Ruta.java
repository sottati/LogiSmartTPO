package com.logismart.dominio.ruta;

import com.logismart.dominio.usuario.Transportista;
import com.logismart.dominio.vehiculo.Vehiculo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ruta {

    private final String id;
    private double distanciaKm;
    private int    duracionEstimadaMin;
    private String estado;

    private Transportista transportistaAsignado;
    private Vehiculo      vehiculoAsignado;
    private final List<PuntoEntrega> paradas = new ArrayList<>();

    public Ruta(String id, double distanciaKm, int duracionEstimadaMin, String estado) {
        this.id                 = id;
        this.distanciaKm        = distanciaKm;
        this.duracionEstimadaMin = duracionEstimadaMin;
        this.estado             = estado;
    }

    public void agregarParada(PuntoEntrega punto) { paradas.add(punto); }

    public void agregarParada(String direccion, double lat, double lng,
                              String ventana, int orden) {
        paradas.add(new PuntoEntrega(direccion, lat, lng, ventana, orden));
    }

    public void optimizar() {
        paradas.sort(Comparator.comparingInt(PuntoEntrega::getOrdenParada));
        recalcular();
    }

    public void recalcular() { distanciaKm = calcularDistanciaTotal(); }

    public double calcularDistanciaTotal() {
        if (paradas.size() < 2) return 0.0;
        double total = 0.0;
        for (int i = 1; i < paradas.size(); i++) {
            PuntoEntrega a = paradas.get(i - 1);
            PuntoEntrega b = paradas.get(i);
            total += PosicionGPS.haversineKm(a.getLat(), a.getLng(), b.getLat(), b.getLng());
        }
        distanciaKm = total;
        return distanciaKm;
    }

    public double calcularCostoEstimado() {
        double costoKm = vehiculoAsignado != null ? vehiculoAsignado.getCostoBaseKm() : 1.0;
        return calcularDistanciaTotal() * costoKm;
    }

    public void asignarTransportista(Transportista t) { transportistaAsignado = t; }
    public void asignarVehiculo(Vehiculo v)           { vehiculoAsignado = v; }
    public void iniciar()                             { estado = "EN_CURSO"; }
    public void completar()                           { estado = "COMPLETADA"; }

    public String         getId()                       { return id; }
    public double         getDistanciaKm()              { return distanciaKm; }
    public int            getDuracionEstimadaMin()      { return duracionEstimadaMin; }
    public String         getEstado()                   { return estado; }
    public Transportista  getTransportistaAsignado()    { return transportistaAsignado; }
    public Vehiculo       getVehiculoAsignado()         { return vehiculoAsignado; }
    public List<PuntoEntrega> getParadas()              { return Collections.unmodifiableList(paradas); }
}
