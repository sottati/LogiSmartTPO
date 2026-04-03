package com.logismart.dominio;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public final class BCrypt {
	private BCrypt() {
	}

	public static String hash(String password) {
		if (password == null || password.isBlank()) {
			throw new IllegalArgumentException("La password no puede estar vacia");
		}

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No se pudo hashear la password", e);
		}
	}
}