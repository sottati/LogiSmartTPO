package com.logismart.infraestructura.fabrica;

import com.logismart.dominio.usuario.AdminEmpresa;
import com.logismart.dominio.usuario.AdminPlataforma;
import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.dominio.usuario.OperadorLogistico;
import com.logismart.dominio.usuario.Transportista;
import com.logismart.dominio.usuario.Usuario;

import java.util.UUID;

/**
 * Factory Method para crear usuarios por tipo de rol.
 * Ortogonal a la región: un OperadorLogístico es el mismo en AR y BR.
 */
public final class UsuarioFactory {

    private UsuarioFactory() {}

    public static Usuario crear(String tipo, String username, String email) {
        String id = UUID.randomUUID().toString();
        switch (tipo.toLowerCase()) {
            case "operador":          return new OperadorLogistico(id, username, email, "", "ACTIVO");
            case "admin_empresa":     return new AdminEmpresa(id, username, email, "", "ACTIVO");
            case "admin_plataforma":  return new AdminPlataforma(id, username, email, "", "ACTIVO");
            case "transportista":     return new Transportista(id, username, email, "", "ACTIVO", "LIC-" + id.substring(0, 6).toUpperCase());
            case "cliente":           return new ClienteFinal(id, username, email, "", "ACTIVO");
            default: throw new IllegalArgumentException("Tipo de usuario desconocido: " + tipo);
        }
    }
}
