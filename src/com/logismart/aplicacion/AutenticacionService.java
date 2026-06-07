package com.logismart.aplicacion;

import com.logismart.dominio.usuario.Usuario;
import com.logismart.util.BCrypt;

public class AutenticacionService {
	public String hashearPassword(String passwordPlano) {
		return BCrypt.hash(passwordPlano);
	}

	public boolean validarPassword(Usuario usuario, String passwordPlano) {
		if (usuario == null) {
			throw new IllegalArgumentException("El usuario no puede ser nulo");
		}
		return usuario.verificarPassword(hashearPassword(passwordPlano));
	}

	public void cambiarPassword(Usuario usuario, String nuevaPasswordPlano) {
		if (usuario == null) {
			throw new IllegalArgumentException("El usuario no puede ser nulo");
		}
		usuario.cambiarPassword(hashearPassword(nuevaPasswordPlano));
	}
}

