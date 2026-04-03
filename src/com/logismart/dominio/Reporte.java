package com.logismart.dominio;

import java.time.LocalDateTime;

public class Reporte {
	private String id;
	private String periodo;
	private String tipo;
	private LocalDateTime generadoEn;

	public Reporte(String id, String periodo, String tipo, LocalDateTime generadoEn) {
		this.id = id;
		this.periodo = periodo;
		this.tipo = tipo;
		this.generadoEn = generadoEn;
	}

	public String getId() {
		return id;
	}

	public String getPeriodo() {
		return periodo;
	}

	public String getTipo() {
		return tipo;
	}

	public LocalDateTime getGeneradoEn() {
		return generadoEn;
	}

	public void generar() {
		generadoEn = LocalDateTime.now();
	}

	public void exportar() {
	}

	public void compartir() {
	}
}
