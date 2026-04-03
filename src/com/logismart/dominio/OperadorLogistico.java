package com.logismart.dominio;

public class OperadorLogistico extends Usuario {
	private String empresaId;
	private String zonaOperacion;
	private String turno;

	public OperadorLogistico(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			String empresaId,
			String zonaOperacion,
			String turno) {
		super(id, username, email, passwordHash, rol, estado);
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
	
	public void asignarRuta() {
		
	}
}
