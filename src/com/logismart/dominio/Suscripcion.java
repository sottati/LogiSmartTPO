package com.logismart.dominio;

import java.time.LocalDate;

public class Suscripcion {
	private String id;
	private String plan;
	private String estado;
	private double montoMensual;
	private LocalDate proximoVencimiento;

	public Suscripcion(String id, String plan, String estado, double montoMensual, LocalDate proximoVencimiento) {
		this.id = id;
		this.plan = plan;
		this.estado = estado;
		this.montoMensual = montoMensual;
		this.proximoVencimiento = proximoVencimiento;
	}

	public String getId() {
		return id;
	}

	public String getPlan() {
		return plan;
	}

	public String getEstado() {
		return estado;
	}

	public double getMontoMensual() {
		return montoMensual;
	}

	public LocalDate getProximoVencimiento() {
		return proximoVencimiento;
	}

	public void activar() {
		estado = "ACTIVA";
	}

	public void pausar() {
		estado = "PAUSADA";
	}

	public void renovar() {
		proximoVencimiento = proximoVencimiento.plusMonths(1);
		estado = "ACTIVA";
	}

	public void cambiarPlan(String nuevoPlan, double nuevoMontoMensual) {
		plan = nuevoPlan;
		montoMensual = nuevoMontoMensual;
	}
}
