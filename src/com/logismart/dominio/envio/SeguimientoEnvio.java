package com.logismart.dominio.envio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.logismart.dominio.ruta.ETA;
import com.logismart.dominio.ruta.PosicionGPS;

public class SeguimientoEnvio {
	private String id;
	private String estadoActual;
	private String ultimoEvento;
	private LocalDateTime ultimaActualizacion;
	private ETA eta;
	private List<PosicionGPS> historialPosiciones;

	public SeguimientoEnvio(String id, String estadoActual) {
		this.id = id;
		this.estadoActual = estadoActual;
		this.ultimoEvento = "CREADO";
		this.ultimaActualizacion = LocalDateTime.now();
		this.eta = new ETA(0, LocalDateTime.now(), 0.0);
		this.historialPosiciones = new ArrayList<>();
	}

	public SeguimientoEnvio(String id, String estadoActual, String ultimoEvento, LocalDateTime ultimaActualizacion) {
		this(id, estadoActual);
		this.ultimoEvento = ultimoEvento;
		this.ultimaActualizacion = ultimaActualizacion;
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
		return Collections.unmodifiableList(historialPosiciones);
	}

	public ETA getEta() {
		return eta;
	}

	public void actualizarEstado(String nuevoEstado) {
		actualizarEstado(nuevoEstado, ultimoEvento);
	}

	public void actualizarEstado(String nuevoEstado, String nuevoEvento) {
		estadoActual = nuevoEstado;
		ultimoEvento = nuevoEvento;
		ultimaActualizacion = LocalDateTime.now();
	}

	public void registrarPosicion(PosicionGPS posicionGPS) {
		historialPosiciones.add(posicionGPS);
		ultimaActualizacion = LocalDateTime.now();
	}

	public void registrarPosicion(double lat, double lng, double precision) {
		PosicionGPS posicion = new PosicionGPS(lat, lng, LocalDateTime.now(), precision);
		historialPosiciones.add(posicion);
		ultimaActualizacion = LocalDateTime.now();
	}

	public void actualizarETA(int minutos, double nivelConfianza) {
		eta.recalcular(minutos, nivelConfianza);
		ultimaActualizacion = LocalDateTime.now();
	}
}

