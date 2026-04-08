package com.logismart.dominio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Ruta {
	private String id;
	private double distanciaKm;
	private int duracionEstimadaMin;
	private String estado;
	private Transportista transportistaAsignado;
	private Vehiculo vehiculoAsignado;
	private List<PuntoEntrega> paradas;

	public Ruta(String id, double distanciaKm, int duracionEstimadaMin, String estado) {
		this.id = id;
		this.distanciaKm = distanciaKm;
		this.duracionEstimadaMin = duracionEstimadaMin;
		this.estado = estado;
		this.paradas = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public double getDistanciaKm() {
		return distanciaKm;
	}

	public int getDuracionEstimadaMin() {
		return duracionEstimadaMin;
	}

	public String getEstado() {
		return estado;
	}

	public Transportista getTransportistaAsignado() {
		return transportistaAsignado;
	}

	public Vehiculo getVehiculoAsignado() {
		return vehiculoAsignado;
	}

	public List<PuntoEntrega> getParadas() {
		return Collections.unmodifiableList(paradas);
	}

	public void optimizar() {
	}

	public void recalcular() {
	}

	public void asignarTransportista(Transportista transportista) {
		transportistaAsignado = transportista;
	}

	public void asignarVehiculo(Vehiculo vehiculo) {
		vehiculoAsignado = vehiculo;
	}

	public void agregarParada(PuntoEntrega punto) {
		this.paradas.add(punto);
	}

	public void agregarParada(String direccion, double lat, double lng, String ventanaHorario, int ordenParada) {
		PuntoEntrega punto = new PuntoEntrega(direccion, lat, lng, ventanaHorario, ordenParada);
		this.paradas.add(punto);
	}
}
