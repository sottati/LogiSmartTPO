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
	
	public String getPatente() {
		return patente;
	}

	public void setPatente(String patente) {
		this.patente = patente;
	}

	public double getCapacidadKg() {
		return capacidadKg;
	}

	/**
	 * Verifica si el vehiculo tiene capacidad para transportar el peso dado.
	 * Regla de negocio: se usa un margen de seguridad del 90% de la capacidad maxima.
	 * @param pesoTotalKg peso combinado de todas las ordenes del envio
	 * @return true si el vehiculo puede tomar la carga
	 */
	public boolean puedeCargar(double pesoTotalKg) {
		final double MARGEN_SEGURIDAD = 0.90;
		return this.disponibilidad && (pesoTotalKg <= this.capacidadKg * MARGEN_SEGURIDAD);
	}

	/**
	 * Verifica si el vehiculo esta disponible para ser asignado.
	 * Condicion: debe estar disponible y en estado operativo.
	 */
	public boolean estaOperativo() {
		return this.disponibilidad;
		// En una version mas completa: && "OPERATIVO".equals(this.estadoMantenimiento)
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
