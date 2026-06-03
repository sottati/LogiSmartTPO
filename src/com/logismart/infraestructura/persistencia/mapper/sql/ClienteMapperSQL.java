package com.logismart.infraestructura.persistencia.mapper.sql;

import com.logismart.dominio.ClienteFinal;
import com.logismart.infraestructura.persistencia.mapper.ClienteMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementacion SQL del Data Mapper para ClienteFinal.
 *
 * Persiste el subconjunto: id (VARCHAR), nombre, email, telefono.
 * Ignora passwordHash, rol, estado y permisos (responsabilidad del modulo de auth).
 * Tabla esperada: clientes(id VARCHAR PK, nombre, email, telefono)
 *
 * Compila con JDK. No se ejecuta en tests (impl en memoria cubre los tests).
 */
public class ClienteMapperSQL implements ClienteMapper {

    private final Connection connection;

    public ClienteMapperSQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertar(ClienteFinal cliente) {
        String sql = "INSERT INTO clientes (id, nombre, email, telefono) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cliente.getId());
            ps.setString(2, cliente.getNombre());
            ps.setString(3, cliente.getEmail());
            ps.setString(4, cliente.getTelefono());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar cliente: " + cliente.getId(), e);
        }
    }

    @Override
    public void actualizar(ClienteFinal cliente) {
        String sql = "UPDATE clientes SET nombre=?, email=?, telefono=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getEmail());
            ps.setString(3, cliente.getTelefono());
            ps.setString(4, cliente.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cliente: " + cliente.getId(), e);
        }
    }

    @Override
    public void eliminar(String id) {
        String sql = "DELETE FROM clientes WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cliente: " + id, e);
        }
    }

    @Override
    public ClienteFinal buscarPorId(String id) {
        String sql = "SELECT id, nombre, email, telefono FROM clientes WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new ClienteFinal(
                            rs.getString("id"),
                            rs.getString("email"),
                            rs.getString("email"),
                            "",
                            "CLIENTE",
                            "ACTIVO",
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            ""
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cliente: " + id, e);
        }
    }
}
