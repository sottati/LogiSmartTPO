package com.logismart.infraestructura.estructural.fabrica;

import com.logismart.dominio.usuario.AdminEmpresa;
import com.logismart.dominio.usuario.AdminPlataforma;
import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.usuario.OperadorLogistico;
import com.logismart.dominio.usuario.Transportista;
import com.logismart.dominio.usuario.Usuario;

import java.util.UUID;

/**
 * Factory Method para usuarios. Tipos válidos: "cliente", "operador", "admin",
 * "transportista", "admin_plataforma".
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
            case "transportista":
                return new Transportista(id, nombre, "", "", "TRANSPORTISTA", "ACTIVO",
                        "", true, null);
            case "admin_plataforma":
                return new AdminPlataforma(id, nombre, "", "", "ADMIN_PLATAFORMA", "ACTIVO",
                        id, "FULL", "General");
            default:
                throw new IllegalArgumentException("Tipo de usuario desconocido: " + tipo);
        }
    }
}

