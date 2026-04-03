package com.logismart.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Envio {
	private String id;
	private String empresaId;
	private String estado;
	private String prioridad;
	private LocalDateTime fechaProgramada;
	private List<Orden> ordenes;
	private SeguimientoEnvio seguimiento;
	private Entrega entrega;

	public Envio(String id, String empresaId, String estado, String prioridad, LocalDateTime fechaProgramada,
			SeguimientoEnvio seguimiento, Entrega entrega) {
		this.id = id;
		this.empresaId = empresaId;
		this.estado = estado;
		this.prioridad = prioridad;
		this.fechaProgramada = fechaProgramada;
		this.ordenes = new ArrayList<>();
		this.seguimiento = seguimiento;
		this.entrega = entrega;
	}

	public String getId() {
		return id;
	}

	public String getEmpresaId() {
		return empresaId;
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

	public List<Orden> getOrdenes() {
		return Collections.unmodifiableList(ordenes);
	}

	public SeguimientoEnvio getSeguimiento() {
		return seguimiento;
	}

	public Entrega getEntrega() {
		return entrega;
	}

	public void agregarOrden(Orden orden) {
		ordenes.add(orden);
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
