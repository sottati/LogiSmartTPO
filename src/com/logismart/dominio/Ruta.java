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
		distanciaKm = calcularDistanciaTotal();
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

	public double calcularDistanciaTotal() {
		if (paradas.size() < 2) {
			distanciaKm = 0.0;
			return distanciaKm;
		}

		double total = 0.0;
		for (int i = 1; i < paradas.size(); i++) {
			PuntoEntrega origen = paradas.get(i - 1);
			PuntoEntrega destino = paradas.get(i);
			total += distanciaEntre(origen.getLat(), origen.getLng(), destino.getLat(), destino.getLng());
		}

		distanciaKm = total;
		return distanciaKm;
	}

	public double calcularCostoEstimado() {
		double costoBaseKm = 1.0;
		if (vehiculoAsignado != null) {
			String tipo = vehiculoAsignado.getTipo();
			if ("CAMION".equalsIgnoreCase(tipo)) {
				costoBaseKm = 1.8;
			} else if ("UTILITARIO".equalsIgnoreCase(tipo)) {
				costoBaseKm = 1.3;
			}
		}
		return calcularDistanciaTotal() * costoBaseKm;
	}

	private double distanciaEntre(double lat1, double lng1, double lat2, double lng2) {
		double radioTierraKm = 6371.0;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
				* Math.sin(dLng / 2) * Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return radioTierraKm * c;
	}
}
