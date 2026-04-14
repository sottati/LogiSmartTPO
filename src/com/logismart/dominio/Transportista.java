package com.logismart.dominio;

public class Transportista extends Usuario {
	private String licencia;
	private boolean disponibilidad;
	private Vehiculo vehiculoAsignado;

	public Transportista(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			String licencia,
			boolean disponibilidad,
			Vehiculo vehiculoAsignado) {
		super(id, username, email, passwordHash, rol, estado);
		this.licencia = licencia;
		this.disponibilidad = disponibilidad;
		this.vehiculoAsignado = vehiculoAsignado;
	}

	public String getLicencia() {
		return licencia;
	}

	public void setLicencia(String licencia) {
		this.licencia = licencia;
	}

	public boolean isDisponibilidad() {
		return disponibilidad;
	}

	public void setDisponibilidad(boolean disponibilidad) {
		this.disponibilidad = disponibilidad;
	}

	public Vehiculo getVehiculoAsignado() {
		return vehiculoAsignado;
	}

	public void setVehiculoAsignado(Vehiculo vehiculoAsignado) {
		this.vehiculoAsignado = vehiculoAsignado;
	}
	
	public void iniciarRecorrido() {
		
	}

	public void actualizarPosicion() {
		
	}
	
	public void registrarEntrega() {
		
	}

	public void reportarIncidente() {
		
	}
}
