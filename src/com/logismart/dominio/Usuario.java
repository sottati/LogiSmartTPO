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

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
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
	
	public void cambiarPassword(String newPassword) {
		this.passwordHash = newPassword;
	}

	public void cerrarSesion() {
		
	}
	
}
