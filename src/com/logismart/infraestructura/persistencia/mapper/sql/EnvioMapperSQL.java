package com.logismart.infraestructura.persistencia.mapper.sql;

import com.logismart.dominio.envio.Envio;
import com.logismart.infraestructura.persistencia.mapper.EnvioMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementacion SQL del Data Mapper para Envio.
 *
 * Usa PreparedStatement con SQL real — compila con el JDK sin driver adicional.
 * No se ejecuta en los tests (no hay base de datos en el build); los tests
 * usan RepositorioEnvioMemoria. Esta clase demuestra dominio de JDBC y la
 * arquitectura de la capa de datos.
 *
 * Tabla esperada: envios(id VARCHAR PK, origen, destino, peso DOUBLE,
 *                         estado, costo DOUBLE, metodo_pago, tipo)
 *
 * Decision de diseno: EnvioBuilder reconstruye el Envio desde la fila;
 * el id de la columna es VARCHAR, no INT (fidelidad al dominio real).
 */
public class EnvioMapperSQL implements EnvioMapper {

    private final Connection connection;

    public EnvioMapperSQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertar(Envio envio) {
        String sql = "INSERT INTO envios (id, origen, destino, peso, estado, costo, metodo_pago, tipo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, envio.getId());
            ps.setString(2, envio.getOrigen());
            ps.setString(3, envio.getDestino());
            ps.setDouble(4, envio.getPeso());
            ps.setString(5, envio.getEstado());
            ps.setDouble(6, envio.getCosto());
            ps.setString(7, envio.getMetodoPago());
            ps.setString(8, envio.getTipo());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar envio: " + envio.getId(), e);
        }
    }

    @Override
    public void actualizar(Envio envio) {
        String sql = "UPDATE envios SET origen=?, destino=?, peso=?, estado=?, "
                   + "costo=?, metodo_pago=?, tipo=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, envio.getOrigen());
            ps.setString(2, envio.getDestino());
            ps.setDouble(3, envio.getPeso());
            ps.setString(4, envio.getEstado());
            ps.setDouble(5, envio.getCosto());
            ps.setString(6, envio.getMetodoPago());
            ps.setString(7, envio.getTipo());
            ps.setString(8, envio.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar envio: " + envio.getId(), e);
        }
    }

    @Override
    public void eliminar(String id) {
        String sql = "DELETE FROM envios WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar envio: " + id, e);
        }
    }

    /**
     * Reconstruye el Envio desde la fila usando EnvioBuilder.
     * Fidelidad al dominio real: no usa constructor de 4 args sino el Builder.
     */
    @Override
    public Envio buscarPorId(String id) {
        String sql = "SELECT id, origen, destino, peso, estado, costo, metodo_pago, tipo "
                   + "FROM envios WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Envio.EnvioBuilder(
                            rs.getString("id"),
                            rs.getString("origen"),
                            rs.getString("destino"))
                        .peso(rs.getDouble("peso"))
                        .estado(rs.getString("estado"))
                        .costo(rs.getDouble("costo"))
                        .metodoPago(rs.getString("metodo_pago"))
                        .tipo(rs.getString("tipo"))
                        .build();
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar envio: " + id, e);
        }
    }
}

