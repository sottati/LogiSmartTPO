package com.logismart.dominio;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Envio {
	private String id;
	private Empresa empresa;
	private String estado;
	private String prioridad;
	private LocalDateTime fechaProgramada;
	private List<Orden> ordenes;
	private SeguimientoEnvio seguimiento;
	private Entrega entrega;

	public Envio(String id, Empresa empresa, String prioridad, LocalDateTime fechaProgramada) {
		this.id = id;
		this.empresa = empresa;
		this.estado = "PENDIENTE";
		this.prioridad = prioridad;
		this.fechaProgramada = fechaProgramada;
		this.ordenes = new ArrayList<>();
		this.seguimiento = new SeguimientoEnvio(id + "-seg", "PENDIENTE");
		this.entrega = new Entrega(id + "-ent");
	}

	public String getId() {
		return id;
	}

	public Empresa getEmpresa() {
		return empresa;
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
		orden.asociarEnvio(this);
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
