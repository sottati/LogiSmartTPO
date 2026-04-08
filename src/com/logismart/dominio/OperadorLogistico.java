package com.logismart.dominio;

import java.time.LocalDateTime;
import java.util.UUID;

public class OperadorLogistico extends Usuario {
	private String empresaId;
	private String zonaOperacion;
	private String turno;

	public OperadorLogistico(
			String id,
			String username,
			String email,
			String password,
			String rol,
			String estado,
			String empresaId,
			String zonaOperacion,
			String turno) {
		super(id, username, email, password, rol, estado);
		this.empresaId = empresaId;
		this.zonaOperacion = zonaOperacion;
		this.turno = turno;
	}

	public String getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(String empresaId) {
		this.empresaId = empresaId;
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
		return new Envio(UUID.randomUUID().toString(), empresaId, prioridad, fechaProgramada);
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
