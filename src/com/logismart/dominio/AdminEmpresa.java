package com.logismart.dominio;

public class AdminEmpresa extends Usuario {
	private String empresaId;
	private String permisosAdmin;
	private String notificaciones;

	public AdminEmpresa(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			String empresaId,
			String permisosAdmin,
			String notificaciones) {
		super(id, username, email, passwordHash, rol, estado);
		this.empresaId = empresaId;
		this.permisosAdmin = permisosAdmin;
		this.notificaciones = notificaciones;
	}

	public String getEmpresaId() {
		return empresaId;
	}

	public void setEmpresaId(String empresaId) {
		this.empresaId = empresaId;
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
