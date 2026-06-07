package com.logismart.infraestructura.persistencia.repositorio.sql;

import com.logismart.dominio.usuario.ClienteFinal;
import com.logismart.infraestructura.persistencia.mapper.sql.ClienteMapperSQL;
import com.logismart.infraestructura.persistencia.repositorio.RepositorioCliente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion SQL de RepositorioCliente.
 * Delega en ClienteMapperSQL para las operaciones CRUD individuales.
 * Compila con JDK. No se ejecuta en tests.
 */
public class RepositorioClienteSQL implements RepositorioCliente {

    private final ClienteMapperSQL mapper;
    private final Connection connection;

    public RepositorioClienteSQL(Connection connection) {
        this.connection = connection;
        this.mapper = new ClienteMapperSQL(connection);
    }

    @Override
    public void guardar(ClienteFinal cliente) {
        mapper.insertar(cliente);
    }

    @Override
    public ClienteFinal obtener(String id) {
        return mapper.buscarPorId(id);
    }

    @Override
    public ClienteFinal obtener(int id) {
        return null;
    }

    @Override
    public List<ClienteFinal> obtenerTodos() {
        String sql = "SELECT id, nombre, email, telefono FROM clientes";
        List<ClienteFinal> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new ClienteFinal(
                        rs.getString("id"),
                        rs.getString("email"),
                        rs.getString("email"),
                        "", "CLIENTE", "ACTIVO",
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        ""));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los clientes", e);
        }
        return lista;
    }

    @Override
    public void eliminar(int id) {
        // no-op
    }

    @Override
    public List<ClienteFinal> buscarPorNombre(String nombre) {
        String sql = "SELECT id, nombre, email, telefono FROM clientes WHERE nombre LIKE ?";
        List<ClienteFinal> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, "%" + nombre + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new ClienteFinal(
                            rs.getString("id"),
                            rs.getString("email"),
                            rs.getString("email"),
                            "", "CLIENTE", "ACTIVO",
                            rs.getString("nombre"),
                            rs.getString("telefono"),
                            ""));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar clientes por nombre: " + nombre, e);
        }
        return lista;
    }
}

