package com.logismart.infraestructura.fabrica;

import com.logismart.dominio.usuario.AdminEmpresa;
import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.usuario.OperadorLogistico;
import com.logismart.dominio.usuario.Usuario;

import java.util.UUID;

/**
 * Factory Method para usuarios - crea ClienteFinal, OperadorLogistico o AdminEmpresa
 * a partir de un identificador de tipo en String.
 */
public final class UsuarioFactory {

    private UsuarioFactory() {}

    public static Usuario crearUsuario(String tipo, String nombre) {
        String id = UUID.randomUUID().toString();
        switch (tipo.toLowerCase()) {
            case "cliente":
                return new ClienteFinal(id, nombre, "", "", "CLIENTE", "ACTIVO",
                        nombre, "", "");
            case "operador":
                return new OperadorLogistico(id, nombre, "", "", "OPERADOR", "ACTIVO",
                        null, "General", "Mañana");
            case "admin":
                return new AdminEmpresa(id, nombre, "", "", "ADMIN_EMPRESA", "ACTIVO",
                        null, "FULL", "EMAIL");
            default:
                throw new IllegalArgumentException("Tipo de usuario desconocido: " + tipo);
        }
    }
}

