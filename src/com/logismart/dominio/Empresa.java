package com.logismart.dominio;

import java.time.LocalDate;

public class Empresa {
	private String id;
	private String razonSocial;
	private String cuit;
	private String estado;
	private LocalDate fechaAlta;

	public Empresa(String id, String razonSocial, String cuit, String estado, LocalDate fechaAlta) {
		this.id = id;
		this.razonSocial = razonSocial;
		this.cuit = cuit;
		this.estado = estado;
		this.fechaAlta = fechaAlta;
	}

	public String getId() {
		return id;
	}

	public String getRazonSocial() {
		return razonSocial;
	}

	public String getCuit() {
		return cuit;
	}

	public String getEstado() {
		return estado;
	}

	public LocalDate getFechaAlta() {
		return fechaAlta;
	}

	public void activar() {
		estado = "ACTIVA";
	}

	public void suspender() {
		estado = "SUSPENDIDA";
	}

	public void actualizarDatos(String nuevaRazonSocial, String nuevoCuit) {
		razonSocial = nuevaRazonSocial;
		cuit = nuevoCuit;
	}
}
