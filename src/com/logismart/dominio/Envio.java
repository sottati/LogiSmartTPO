package com.logismart.dominio;

import java.time.LocalDateTime;

public class Envio {
	private String id;
	private String estado;
	private String prioridad;
	private LocalDateTime fechaProgramada;

	public Envio(String id, String estado, String prioridad, LocalDateTime fechaProgramada) {
		this.id = id;
		this.estado = estado;
		this.prioridad = prioridad;
		this.fechaProgramada = fechaProgramada;
	}

	public String getId() {
		return id;
	}

	public String getEstado() {
		return estado;
	}

	public String getPrioridad() {
		return prioridad;
	}

	public LocalDateTime getFechaProgramada() {
		return fechaProgramada;
	}

	public void reprogramar(LocalDateTime nuevaFechaProgramada) {
		fechaProgramada = nuevaFechaProgramada;
	}

	public void iniciar() {
		estado = "EN_CURSO";
	}

	public void cancelar() {
		estado = "CANCELADO";
	}

	public void cerrar() {
		estado = "CERRADO";
	}
}
