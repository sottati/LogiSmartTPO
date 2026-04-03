package com.logismart.dominio;

import java.time.LocalDateTime;

public class ETA {
	private int valorMinutos;
	private LocalDateTime actualizadoEn;
	private double nivelConfianza;

	public ETA(int valorMinutos, LocalDateTime actualizadoEn, double nivelConfianza) {
		this.valorMinutos = valorMinutos;
		this.actualizadoEn = actualizadoEn;
		this.nivelConfianza = nivelConfianza;
	}

	public int getValorMinutos() {
		return valorMinutos;
	}

	public LocalDateTime getActualizadoEn() {
		return actualizadoEn;
	}

	public double getNivelConfianza() {
		return nivelConfianza;
	}

	public void recalcular(int nuevoValorMinutos, double nuevoNivelConfianza) {
		valorMinutos = nuevoValorMinutos;
		nivelConfianza = nuevoNivelConfianza;
		actualizadoEn = LocalDateTime.now();
	}

	public boolean vencio() {
		return actualizadoEn.isBefore(LocalDateTime.now().minusMinutes(30));
	}
}
