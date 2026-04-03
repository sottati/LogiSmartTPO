package com.logismart.dominio;

public class Ruta {
	private String id;
	private double distanciaKm;
	private int duracionEstimadaMin;
	private String estado;
	private Transportista transportistaAsignado;
	private Vehiculo vehiculoAsignado;

	public Ruta(String id, double distanciaKm, int duracionEstimadaMin, String estado) {
		this.id = id;
		this.distanciaKm = distanciaKm;
		this.duracionEstimadaMin = duracionEstimadaMin;
		this.estado = estado;
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
}
