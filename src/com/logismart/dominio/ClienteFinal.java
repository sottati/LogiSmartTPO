package com.logismart.dominio;

public class ClienteFinal extends Usuario {
	private String nombre;
	private String telefono;
	private String direccionEntrega;

	public ClienteFinal(
			String id,
			String username,
			String email,
			String password,
			String rol,
			String estado,
			String nombre,
			String telefono,
			String direccionEntrega) {
		super(id, username, email, password, rol, estado);
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

}
