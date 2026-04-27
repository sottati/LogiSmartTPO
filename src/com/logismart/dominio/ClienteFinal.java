package com.logismart.dominio;

public class ClienteFinal extends Usuario implements IPermisos {
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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccionEntrega() {
		return direccionEntrega;
	}

	public void setDireccionEntrega(String direccionEntrega) {
		this.direccionEntrega = direccionEntrega;
	}
	
	public void consultarTracking() {
	}

	public void verETA() {
	}

	public void confirmarRecepcion() {
	}

	@Override
	public void saludar() {
		System.out.println("[Cliente] Hola, soy el cliente " + getNombre() + ".");
	}

	// IPermisos: ClienteFinal solo puede consultar su propio envio
	@Override
	public boolean puedeCrearEnvio() {
		return false;
	}

	@Override
	public boolean puedeAsignarRuta() {
		return false;
	}

	@Override
	public boolean puedeVerReportes() {
		return false;
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
