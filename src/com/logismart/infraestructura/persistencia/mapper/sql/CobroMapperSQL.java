package com.logismart.infraestructura.persistencia.mapper.sql;

import com.logismart.dominio.empresa.Cobro;
import com.logismart.infraestructura.persistencia.mapper.CobroMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Implementacion SQL del Data Mapper para Cobro.
 * Tabla esperada: cobros(id VARCHAR PK, monto DOUBLE, estado, fecha TIMESTAMP,
 *                        medio_pago, envio_id VARCHAR FK)
 *
 * El campo envio_id fue agregado aditivamente en Hito 13 para soportar
 * la busqueda buscarPorEnvio en RepositorioPago.
 * Compila con JDK. No se ejecuta en tests (impl en memoria cubre los tests).
 */
public class CobroMapperSQL implements CobroMapper {

    private final Connection connection;

    public CobroMapperSQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertar(Cobro cobro) {
        String sql = "INSERT INTO cobros (id, monto, estado, fecha, medio_pago, envio_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cobro.getId());
            ps.setDouble(2, cobro.getMonto());
            ps.setString(3, cobro.getEstado());
            ps.setTimestamp(4, cobro.getFecha() != null
                    ? Timestamp.valueOf(cobro.getFecha()) : null);
            ps.setString(5, cobro.getMedioPago());
            ps.setString(6, cobro.getEnvioId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar cobro: " + cobro.getId(), e);
        }
        denormalizarMetodoPago(cobro);
    }

    @Override
    public void actualizar(Cobro cobro) {
        String sql = "UPDATE cobros SET monto=?, estado=?, fecha=?, medio_pago=?, envio_id=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, cobro.getMonto());
            ps.setString(2, cobro.getEstado());
            ps.setTimestamp(3, cobro.getFecha() != null
                    ? Timestamp.valueOf(cobro.getFecha()) : null);
            ps.setString(4, cobro.getMedioPago());
            ps.setString(5, cobro.getEnvioId());
            ps.setString(6, cobro.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar cobro: " + cobro.getId(), e);
        }
        denormalizarMetodoPago(cobro);
    }

    /**
     * Desnormalización controlada: propaga metodo_pago al envío asociado.
     * El Cobro es el dueño del dato; EnvioMapperSQL solo tiene NULL porque
     * Envio no conoce su propio pago. Este mapper actúa como única fuente
     * de verdad para esa columna.
     */
    private void denormalizarMetodoPago(Cobro cobro) {
        if (cobro.getEnvioId() == null) return;
        String sql = "UPDATE envios SET metodo_pago=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, cobro.getMedioPago());
            ps.setString(2, cobro.getEnvioId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(
                "Error al denormalizar metodo_pago en envio: " + cobro.getEnvioId(), e);
        }
    }

    @Override
    public void eliminar(String id) {
        String sql = "DELETE FROM cobros WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar cobro: " + id, e);
        }
    }

    @Override
    public Cobro buscarPorId(String id) {
        String sql = "SELECT id, monto, estado, fecha, medio_pago, envio_id FROM cobros WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("fecha");
                    LocalDateTime fecha = ts != null ? ts.toLocalDateTime() : null;
                    Cobro cobro = new Cobro(
                            rs.getString("id"),
                            rs.getDouble("monto"),
                            rs.getString("estado"),
                            fecha,
                            rs.getString("medio_pago")
                    );
                    cobro.setEnvioId(rs.getString("envio_id"));
                    return cobro;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cobro: " + id, e);
        }
    }
}

