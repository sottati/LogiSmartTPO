package com.logismart.dominio.usuario;

import com.logismart.dominio.empresa.Empresa;

public class AdminEmpresa extends Usuario implements IPermisos {
	private static final Rol ROL = Rol.ADMIN_EMPRESA;

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

	public Empresa getEmpresa()                          { return empresa; }
	public void setEmpresa(Empresa empresa)              { this.empresa = empresa; }
	public String getPermisosAdmin()                     { return permisosAdmin; }
	public void setPermisosAdmin(String permisosAdmin)   { this.permisosAdmin = permisosAdmin; }
	public String getNotificaciones()                    { return notificaciones; }
	public void setNotificaciones(String notificaciones) { this.notificaciones = notificaciones; }

	public void configurarEmpresa() {
		System.out.println("[AdminEmpresa] Configurando empresa...");
	}

	public void consultarReportes() {
		System.out.println("[AdminEmpresa] Consultando reportes de empresa...");
	}

	public void gestionarSuscripcion() {
		System.out.println("[AdminEmpresa] Gestionando suscripción...");
	}

	public String getNombre() { return getUsername(); }

	@Override
	public void saludar() {
		System.out.println("[AdminEmpresa] Hola, soy el administrador de empresa " + getUsername() + ".");
	}

	@Override public boolean puedeCrearEnvio()          { return ROL.puedeCrearEnvio(); }
	@Override public boolean puedeAsignarRuta()         { return ROL.puedeAsignarRuta(); }
	@Override public boolean puedeVerReportes()         { return ROL.puedeVerReportes(); }
	@Override public boolean puedeGestionarFlota()      { return ROL.puedeGestionarFlota(); }
	@Override public boolean puedeAdministrarEmpresas() { return ROL.puedeAdministrarEmpresas(); }
}

