package com.logismart.dominio.usuario;

public class ClienteFinal extends Usuario implements IPermisos {
	private static final Rol ROL = Rol.CLIENTE;

	private String nombre;
	private String telefono;
	private String direccionEntrega;

	public ClienteFinal(
			String id,
			String username,
			String email,
			String passwordHash,
			String rol,
			String estado,
			String nombre,
			String telefono,
			String direccionEntrega) {
		super(id, username, email, passwordHash, rol, estado);
		this.nombre = nombre;
		this.telefono = telefono;
		this.direccionEntrega = direccionEntrega;
	}

	public String getNombre()                  { return nombre; }
	public void setNombre(String nombre)       { this.nombre = nombre; }
	public String getTelefono()                { return telefono; }
	public void setTelefono(String telefono)   { this.telefono = telefono; }
	public String getDireccionEntrega()        { return direccionEntrega; }
	public void setDireccionEntrega(String dir){ this.direccionEntrega = dir; }

	public void consultarTracking() {
		System.out.println("[ClienteFinal] Consultando tracking de envíos...");
	}

	public void verETA() {
		System.out.println("[ClienteFinal] Consultando ETA de entrega...");
	}

	public void confirmarRecepcion() {
		System.out.println("[ClienteFinal] Recepción confirmada por " + nombre + ".");
	}

	@Override
	public void saludar() {
		System.out.println("[Cliente] Hola, soy el cliente " + getNombre() + ".");
	}

	@Override public boolean puedeCrearEnvio()          { return ROL.puedeCrearEnvio(); }
	@Override public boolean puedeAsignarRuta()         { return ROL.puedeAsignarRuta(); }
	@Override public boolean puedeVerReportes()         { return ROL.puedeVerReportes(); }
	@Override public boolean puedeGestionarFlota()      { return ROL.puedeGestionarFlota(); }
	@Override public boolean puedeAdministrarEmpresas() { return ROL.puedeAdministrarEmpresas(); }
}

