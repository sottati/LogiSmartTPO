package com.logismart.dominio;

import com.logismart.util.BCrypt;

public class Usuario {
	private String id;
	private String username;
	private String email;
	private String passwordHash;
	private String rol;
	private String estado;
	
	public Usuario(String id, String username, String email, String password, String rol, String estado) {
		this.id = id;
		this.username = username;
		this.email = email;
		this.passwordHash = BCrypt.hash(password);
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

	public boolean verificarPassword(String password) {
		if (password == null || password.isBlank()) {
			return false;
		}
		return passwordHash.equals(BCrypt.hash(password));
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
		if (newPassword == null || newPassword.isBlank()) {
			throw new IllegalArgumentException("La nueva password no puede estar vacia");
		}
		this.passwordHash = BCrypt.hash(newPassword);
	}

	public void cerrarSesion() {
		
	}
	
}
