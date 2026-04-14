package com.logismart.dominio;

public class AdminEmpresa extends Usuario {
	private Empresa empresa;
	private String permisosAdmin;
	private String notificaciones;

	public AdminEmpresa(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			Empresa empresa,
			String permisosAdmin,
			String notificaciones) {
		super(id, username, email, passwordHash, rol, estado);
		this.empresa = empresa;
		this.permisosAdmin = permisosAdmin;
		this.notificaciones = notificaciones;
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public String getPermisosAdmin() {
		return permisosAdmin;
	}

	public void setPermisosAdmin(String permisosAdmin) {
		this.permisosAdmin = permisosAdmin;
	}

	public String getNotificaciones() {
		return notificaciones;
	}

	public void setNotificaciones(String notificaciones) {
		this.notificaciones = notificaciones;
	}
	
	public void configurarEmpresa() {
	}
	
	public void consultarReportes() {
	}

	public void gestionarSuscripcion() {
	}

}
