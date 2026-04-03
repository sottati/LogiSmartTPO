package com.logismart.dominio;

import java.time.LocalDateTime;

public class Cobro {
	private String id;
	private double monto;
	private String estado;
	private LocalDateTime fecha;
	private String medioPago;

	public Cobro(String id, double monto, String estado, LocalDateTime fecha, String medioPago) {
		this.id = id;
		this.monto = monto;
		this.estado = estado;
		this.fecha = fecha;
		this.medioPago = medioPago;
	}

	public String getId() {
		return id;
	}

	public double getMonto() {
		return monto;
	}

	public String getEstado() {
		return estado;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public String getMedioPago() {
		return medioPago;
	}

	public void autorizar() {
		estado = "AUTORIZADO";
	}

	public void registrarPago() {
		estado = "PAGADO";
		fecha = LocalDateTime.now();
	}

	public void marcarFallido() {
		estado = "FALLIDO";
	}

	public String emitirComprobante() {
		return "COMP-" + id;
	}
}
