package com.logismart.dominio;

import java.time.LocalDateTime;

public class Metrica {
	private String nombre;
	private double valor;
	private String unidad;
	private LocalDateTime timestamp;

	public Metrica(String nombre, double valor, String unidad, LocalDateTime timestamp) {
		this.nombre = nombre;
		this.valor = valor;
		this.unidad = unidad;
		this.timestamp = timestamp;
	}

	public String getNombre() {
		return nombre;
	}

	public double getValor() {
		return valor;
	}

	public String getUnidad() {
		return unidad;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void actualizarValor(double nuevoValor) {
		valor = nuevoValor;
		timestamp = LocalDateTime.now();
	}

	public void normalizar(double divisor) {
		if (divisor != 0) {
			valor = valor / divisor;
			timestamp = LocalDateTime.now();
		}
	}

	public int comparar(Metrica otra) {
		return Double.compare(valor, otra.valor);
	}
}
