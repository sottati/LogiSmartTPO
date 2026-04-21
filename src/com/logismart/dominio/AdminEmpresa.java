package com.logismart.dominio;

public class AdminEmpresa extends Usuario implements IPermisos {
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

	// IPermisos: AdminEmpresa gestiona su empresa, consulta reportes, pero no asigna rutas
	@Override
	public boolean puedeCrearEnvio() {
		return true;
	}

	@Override
	public boolean puedeAsignarRuta() {
		return false;
	}

	@Override
	public boolean puedeVerReportes() {
		return true;
	}

	@Override
	public boolean puedeGestionarFlota() {
		return false;
	}

	@Override
	public boolean puedeAdministrarEmpresas() {
		return false;
	}
}
