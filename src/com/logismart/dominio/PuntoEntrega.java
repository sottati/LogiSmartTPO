package com.logismart.dominio;

public class PuntoEntrega {
	private String direccion;
	private double lat;
	private double lng;
	private String ventanaHorario;
	private int ordenParada;

	public PuntoEntrega(String direccion, double lat, double lng, String ventanaHorario, int ordenParada) {
		this.direccion = direccion;
		this.lat = lat;
		this.lng = lng;
		this.ventanaHorario = ventanaHorario;
		this.ordenParada = ordenParada;
	}

	public String getDireccion() {
		return direccion;
	}

	public double getLat() {
		return lat;
	}

	public double getLng() {
		return lng;
	}

	public String getVentanaHorario() {
		return ventanaHorario;
	}

	public int getOrdenParada() {
		return ordenParada;
	}

	public boolean validarVentana() {
		return ventanaHorario != null && !ventanaHorario.isBlank();
	}

	public void actualizarCoordenadas(double nuevaLat, double nuevaLng) {
		lat = nuevaLat;
		lng = nuevaLng;
	}
}
