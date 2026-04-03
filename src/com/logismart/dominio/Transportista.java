package com.logismart.dominio;

public class Transportista extends Usuario {
	private String licencia;
	private boolean disponibilidad;
	private String vehiculoAsignadoId;

	public Transportista(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			String licencia,
			boolean disponibilidad,
			String vehiculoAsignadoId) {
		super(id, username, email, passwordHash, rol, estado);
		this.licencia = licencia;
		this.disponibilidad = disponibilidad;
		this.vehiculoAsignadoId = vehiculoAsignadoId;
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

	public String getVehiculoAsignadoId() {
		return vehiculoAsignadoId;
	}

	public void setVehiculoAsignadoId(String vehiculoAsignadoId) {
		this.vehiculoAsignadoId = vehiculoAsignadoId;
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
