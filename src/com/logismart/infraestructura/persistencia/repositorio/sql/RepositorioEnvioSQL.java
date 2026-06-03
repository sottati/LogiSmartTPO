package com.logismart.infraestructura.persistencia.repositorio.sql;

import com.logismart.dominio.Envio;
import com.logismart.infraestructura.persistencia.mapper.sql.EnvioMapperSQL;
import com.logismart.infraestructura.persistencia.repositorio.RepositorioEnvio;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion SQL de RepositorioEnvio.
 * Delega en EnvioMapperSQL para las operaciones CRUD individuales.
 * buscarPorEstado ejecuta una query propia (filtro de coleccion).
 *
 * Compila con JDK. No se ejecuta en tests (impl en memoria cubre los tests).
 */
public class RepositorioEnvioSQL implements RepositorioEnvio {

    private final EnvioMapperSQL mapper;
    private final Connection connection;

    public RepositorioEnvioSQL(Connection connection) {
        this.connection = connection;
        this.mapper = new EnvioMapperSQL(connection);
    }

    @Override
    public void guardar(Envio envio) {
        mapper.insertar(envio);
    }

    @Override
    public Envio obtener(String id) {
        return mapper.buscarPorId(id);
    }

    @Override
    public Envio obtener(int id) {
        return null;
    }

    @Override
    public List<Envio> obtenerTodos() {
        String sql = "SELECT id, origen, destino, peso, estado, costo, metodo_pago, tipo FROM envios";
        List<Envio> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new Envio.EnvioBuilder(
                        rs.getString("id"),
                        rs.getString("origen"),
                        rs.getString("destino"))
                    .peso(rs.getDouble("peso"))
                    .estado(rs.getString("estado"))
                    .costo(rs.getDouble("costo"))
                    .metodoPago(rs.getString("metodo_pago"))
                    .tipo(rs.getString("tipo"))
                    .build());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los envios", e);
        }
        return lista;
    }

    @Override
    public void eliminar(int id) {
        // no-op: Envio usa String id
    }

    @Override
    public List<Envio> buscarPorEstado(String estado) {
        String sql = "SELECT id, origen, destino, peso, estado, costo, metodo_pago, tipo "
                   + "FROM envios WHERE estado=?";
        List<Envio> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, estado);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Envio.EnvioBuilder(
                            rs.getString("id"),
                            rs.getString("origen"),
                            rs.getString("destino"))
                        .peso(rs.getDouble("peso"))
                        .estado(rs.getString("estado"))
                        .costo(rs.getDouble("costo"))
                        .metodoPago(rs.getString("metodo_pago"))
                        .tipo(rs.getString("tipo"))
                        .build());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar envios por estado: " + estado, e);
        }
        return lista;
    }
}
