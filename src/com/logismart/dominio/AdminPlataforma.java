package com.logismart.dominio;

public class AdminPlataforma extends Usuario {
	private String idInterno;
	private String nivelAcceso;
	private String area;

	public AdminPlataforma(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			String idInterno,
			String nivelAcceso,
			String area) {
		super(id, username, email, passwordHash, rol, estado);
		this.idInterno = idInterno;
		this.nivelAcceso = nivelAcceso;
		this.area = area;
	}

	public String getIdInterno() {
		return idInterno;
	}

	public void setIdInterno(String idInterno) {
		this.idInterno = idInterno;
	}

	public String getNivelAcceso() {
		return nivelAcceso;
	}

	public void setNivelAcceso(String nivelAcceso) {
		this.nivelAcceso = nivelAcceso;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public void darAltaEmpresa() {
		
	}
	
	public void darBajaEmpresa() {
		
	}
	
	public void monitorearServicio() {
		
	}

	public void gestionarBilling() {
		
	}
}
