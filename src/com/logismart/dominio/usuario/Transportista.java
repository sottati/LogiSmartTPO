package com.logismart.dominio.usuario;

import com.logismart.dominio.vehiculo.Vehiculo;

public class Transportista extends Usuario implements IPermisos {
	private static final Rol ROL = Rol.TRANSPORTISTA;

	private String licencia;
	private boolean disponibilidad;
	private Vehiculo vehiculoAsignado;

	public Transportista(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			String licencia,
			boolean disponibilidad,
			Vehiculo vehiculoAsignado) {
		super(id, username, email, passwordHash, rol, estado);
		this.licencia = licencia;
		this.disponibilidad = disponibilidad;
		this.vehiculoAsignado = vehiculoAsignado;
	}

	public String getLicencia()                       { return licencia; }
	public void setLicencia(String licencia)          { this.licencia = licencia; }
	public boolean isDisponibilidad()                 { return disponibilidad; }
	public void setDisponibilidad(boolean disp)       { this.disponibilidad = disp; }
	public Vehiculo getVehiculoAsignado()             { return vehiculoAsignado; }
	public void setVehiculoAsignado(Vehiculo v)       { this.vehiculoAsignado = v; }

	public void iniciarRecorrido() {
		disponibilidad = false;
		System.out.println("[Transportista] Recorrido iniciado. Transportista no disponible.");
	}

	public void actualizarPosicion() {
		System.out.println("[Transportista] Posición actualizada.");
	}

	public void registrarEntrega() {
		disponibilidad = true;
		System.out.println("[Transportista] Entrega registrada. Transportista disponible.");
	}

	public void reportarIncidente() {
		System.out.println("[Transportista] Incidente reportado.");
	}

	@Override
	public void saludar() {
		System.out.println("[Transportista] Hola, soy el transportista " + getUsername() + ".");
	}

	@Override public boolean puedeCrearEnvio()          { return ROL.puedeCrearEnvio(); }
	@Override public boolean puedeAsignarRuta()         { return ROL.puedeAsignarRuta(); }
	@Override public boolean puedeVerReportes()         { return ROL.puedeVerReportes(); }
	@Override public boolean puedeGestionarFlota()      { return ROL.puedeGestionarFlota(); }
	@Override public boolean puedeAdministrarEmpresas() { return ROL.puedeAdministrarEmpresas(); }
}

