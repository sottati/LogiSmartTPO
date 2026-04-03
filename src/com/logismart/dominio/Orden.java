package com.logismart.dominio;

import java.time.LocalDateTime;

public class Orden {
	private String id;
	private String canalOrigen;
	private String estado;
	private LocalDateTime fechaCreacion;
	private int totalBultos;
	private Envio envio;

	public Orden(String id, String canalOrigen, String estado, LocalDateTime fechaCreacion, int totalBultos) {
		this.id = id;
		this.canalOrigen = canalOrigen;
		this.estado = estado;
		this.fechaCreacion = fechaCreacion;
		this.totalBultos = totalBultos;
	}

	public String getId() {
		return id;
	}

	public String getCanalOrigen() {
		return canalOrigen;
	}

	public String getEstado() {
		return estado;
	}

	public LocalDateTime getFechaCreacion() {
		return fechaCreacion;
	}

	public int getTotalBultos() {
		return totalBultos;
	}

	public Envio getEnvio() {
		return envio;
	}

	public void marcarPendienteEnvio() {
		estado = "PENDIENTE_ENVIO";
	}

	public void asociarEnvio(Envio envio) {
		this.envio = envio;
	}

	public void actualizarEstado(String nuevoEstado) {
		estado = nuevoEstado;
	}
}
