package com.logismart.dominio;

import java.time.LocalDateTime;

public class Entrega {
	private String id;
	private String resultado;
	private LocalDateTime fechaHora;
	private String observaciones;
	private String pruebaAdjunta;

	public Entrega(String id, String resultado, LocalDateTime fechaHora, String observaciones) {
		this.id = id;
		this.resultado = resultado;
		this.fechaHora = fechaHora;
		this.observaciones = observaciones;
	}

	public String getId() {
		return id;
	}

	public String getResultado() {
		return resultado;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public String getPruebaAdjunta() {
		return pruebaAdjunta;
	}

	public void confirmarExitosa() {
		resultado = "EXITOSA";
		fechaHora = LocalDateTime.now();
	}

	public void registrarFallida(String nuevasObservaciones) {
		resultado = "FALLIDA";
		observaciones = nuevasObservaciones;
		fechaHora = LocalDateTime.now();
	}

	public void adjuntarPrueba(String pruebaAdjunta) {
		this.pruebaAdjunta = pruebaAdjunta;
	}
}
