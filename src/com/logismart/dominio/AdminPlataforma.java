package com.logismart.dominio;

public class AdminPlataforma extends Usuario implements IPermisos {
	private static final Rol ROL = Rol.ADMIN_PLATAFORMA;

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

	public String getIdInterno()                   { return idInterno; }
	public void setIdInterno(String idInterno)     { this.idInterno = idInterno; }
	public String getNivelAcceso()                 { return nivelAcceso; }
	public void setNivelAcceso(String nivelAcceso) { this.nivelAcceso = nivelAcceso; }
	public String getArea()                        { return area; }
	public void setArea(String area)               { this.area = area; }

	public void darAltaEmpresa() {
		System.out.println("[AdminPlataforma] Alta de empresa registrada por " + getUsername() + ".");
	}

	public void darBajaEmpresa() {
		System.out.println("[AdminPlataforma] Baja de empresa registrada por " + getUsername() + ".");
	}

	public void monitorearServicio() {
		System.out.println("[AdminPlataforma] Monitoreando servicio...");
	}

	public void gestionarBilling() {
		System.out.println("[AdminPlataforma] Gestión de billing iniciada.");
	}

	@Override
	public void saludar() {
		System.out.println("[AdminPlataforma] Hola, soy el administrador de plataforma " + getUsername() + ".");
	}

	@Override public boolean puedeCrearEnvio()          { return ROL.puedeCrearEnvio(); }
	@Override public boolean puedeAsignarRuta()         { return ROL.puedeAsignarRuta(); }
	@Override public boolean puedeVerReportes()         { return ROL.puedeVerReportes(); }
	@Override public boolean puedeGestionarFlota()      { return ROL.puedeGestionarFlota(); }
	@Override public boolean puedeAdministrarEmpresas() { return ROL.puedeAdministrarEmpresas(); }
}
