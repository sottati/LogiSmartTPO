package com.logismart.infraestructura.persistencia.repositorio.sql;

import com.logismart.dominio.Cobro;
import com.logismart.infraestructura.persistencia.mapper.sql.CobroMapperSQL;
import com.logismart.infraestructura.persistencia.repositorio.RepositorioPago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion SQL de RepositorioPago.
 * Delega en CobroMapperSQL para las operaciones CRUD individuales.
 * buscarPorEnvio usa el campo envio_id de la tabla cobros.
 * Compila con JDK. No se ejecuta en tests.
 */
public class RepositorioPagoSQL implements RepositorioPago {

    private final CobroMapperSQL mapper;
    private final Connection connection;

    public RepositorioPagoSQL(Connection connection) {
        this.connection = connection;
        this.mapper = new CobroMapperSQL(connection);
    }

    @Override
    public void guardar(Cobro cobro) {
        mapper.insertar(cobro);
    }

    @Override
    public Cobro obtener(String id) {
        return mapper.buscarPorId(id);
    }

    @Override
    public Cobro obtener(int id) {
        return null;
    }

    @Override
    public List<Cobro> obtenerTodos() {
        String sql = "SELECT id, monto, estado, fecha, medio_pago, envio_id FROM cobros";
        List<Cobro> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("fecha");
                LocalDateTime fecha = ts != null ? ts.toLocalDateTime() : null;
                Cobro cobro = new Cobro(
                        rs.getString("id"),
                        rs.getDouble("monto"),
                        rs.getString("estado"),
                        fecha,
                        rs.getString("medio_pago"));
                cobro.setEnvioId(rs.getString("envio_id"));
                lista.add(cobro);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los cobros", e);
        }
        return lista;
    }

    @Override
    public void eliminar(int id) {
        // no-op
    }

    @Override
    public List<Cobro> buscarPorEnvio(String envioId) {
        String sql = "SELECT id, monto, estado, fecha, medio_pago, envio_id "
                   + "FROM cobros WHERE envio_id=?";
        List<Cobro> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, envioId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("fecha");
                    LocalDateTime fecha = ts != null ? ts.toLocalDateTime() : null;
                    Cobro cobro = new Cobro(
                            rs.getString("id"),
                            rs.getDouble("monto"),
                            rs.getString("estado"),
                            fecha,
                            rs.getString("medio_pago"));
                    cobro.setEnvioId(rs.getString("envio_id"));
                    lista.add(cobro);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cobros por envio: " + envioId, e);
        }
        return lista;
    }
}
