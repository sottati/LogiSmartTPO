package com.logismart.dominio;

public class Usuario {
	private String id;
	private String username;
	private String email;
	private String passwordHash;
	private String rol;
	private String estado;
	
	public Usuario(String id, String username, String email, String passwordHash, String rol, String estado) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.passwordHash = passwordHash;
		this.rol = rol;
		this.estado = estado;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean verificarPassword(String passwordHash) {
		if (passwordHash == null || passwordHash.isBlank()) {
			return false;
		}
		return this.passwordHash.equals(passwordHash);
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public void iniciarSesion(String username, String password) {
		
	}
	
	public void cambiarPassword(String newPasswordHash) {
		if (newPasswordHash == null || newPasswordHash.isBlank()) {
			throw new IllegalArgumentException("La nueva password no puede estar vacia");
		}
		this.passwordHash = newPasswordHash;
	}

	public void cerrarSesion() {
		
	}
	
}
