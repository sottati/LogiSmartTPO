package com.logismart.dominio;

import java.time.LocalDateTime;
import java.util.UUID;

public class OperadorLogistico extends Usuario {
	private Empresa empresa;
	private String zonaOperacion;
	private String turno;

	public OperadorLogistico(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			Empresa empresa,
			String zonaOperacion,
			String turno) {
		super(id, username, email, passwordHash, rol, estado);
		this.empresa = empresa;
		this.zonaOperacion = zonaOperacion;
		this.turno = turno;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getZonaOperacion() {
		return zonaOperacion;
	}

	public void setZonaOperacion(String zonaOperacion) {
		this.zonaOperacion = zonaOperacion;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public void importarOrdenes() {
		
	}

	public void gestionarFlota() {
	}

	public void planificarRuta() {
		
	}

	public Envio crearEnvio(String prioridad, LocalDateTime fechaProgramada) {
		if (empresa == null) {
			throw new IllegalStateException("El operador debe estar asociado a una empresa");
		}
		return new Envio(UUID.randomUUID().toString(), empresa, prioridad, fechaProgramada);
	}

	public Ruta planificarRuta(Vehiculo vehiculo, Transportista transportista) {
		Ruta ruta = new Ruta(UUID.randomUUID().toString(), 0.0, 0, "PLANIFICADA");
		ruta.asignarVehiculo(vehiculo);
		ruta.asignarTransportista(transportista);
		return ruta;
	}
	
	public void asignarRuta() {
		
	}
}
