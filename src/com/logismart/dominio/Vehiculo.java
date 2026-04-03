package com.logismart.dominio;

public class Vehiculo {
	private String id;
	private String patente;
	private double capacidadKg;
	private String tipo;
	private boolean disponibilidad;
	
	public Vehiculo(String id, String patente, double capacidadKg, String tipo, boolean disponibilidad) {
		this.id = id;
		this.patente = patente;
		this.capacidadKg = capacidadKg;
		this.tipo = tipo;
		this.disponibilidad = disponibilidad;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}

	public double getCapacidadKg() {
		return capacidadKg;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public boolean isDisponible() {
		return disponibilidad;
	}

	public void asignarRuta() {
		disponibilidad = false;
	}

	public void liberar() {
		disponibilidad = true;
	}

	public void actualizarCapacidad(double nuevaCapacidadKg) {
		this.capacidadKg = nuevaCapacidadKg;
	}
}
