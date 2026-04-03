package com.logismart.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SeguimientoEnvio {
	private String id;
	private String estadoActual;
	private String ultimoEvento;
	private LocalDateTime ultimaActualizacion;
	private List<PosicionGPS> historialPosiciones;

	public SeguimientoEnvio(String id, String estadoActual, String ultimoEvento, LocalDateTime ultimaActualizacion) {
		this.id = id;
		this.estadoActual = estadoActual;
		this.ultimoEvento = ultimoEvento;
		this.ultimaActualizacion = ultimaActualizacion;
		this.historialPosiciones = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public String getEstadoActual() {
		return estadoActual;
	}

	public String getUltimoEvento() {
		return ultimoEvento;
	}

	public LocalDateTime getUltimaActualizacion() {
		return ultimaActualizacion;
	}

	public List<PosicionGPS> getHistorialPosiciones() {
		return historialPosiciones;
	}

	public void actualizarEstado(String nuevoEstado) {
		estadoActual = nuevoEstado;
		ultimaActualizacion = LocalDateTime.now();
	}

	public void registrarPosicion(PosicionGPS posicionGPS) {
		historialPosiciones.add(posicionGPS);
		ultimaActualizacion = LocalDateTime.now();
	}

	public void publicarTracking() {
	}
}
