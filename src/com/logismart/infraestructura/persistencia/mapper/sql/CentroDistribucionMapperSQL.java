package com.logismart.infraestructura.persistencia.mapper.sql;

import com.logismart.infraestructura.persistencia.entidad.CentroDistribucion;
import com.logismart.infraestructura.persistencia.mapper.CentroDistribucionMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Implementacion SQL del Data Mapper para CentroDistribucion (entidad de persistencia).
 * Tabla esperada: centros_distribucion(id VARCHAR PK, nombre, ubicacion, codigo, capacidad INT, ocupacion INT)
 *
 * Opera sobre la entidad plana snapshot, generada previamente por CentroAssembler.
 * Compila con JDK. No se ejecuta en tests (impl en memoria cubre los tests).
 */
public class CentroDistribucionMapperSQL implements CentroDistribucionMapper {

    private final Connection connection;

    public CentroDistribucionMapperSQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertar(CentroDistribucion centro) {
        String sql = "INSERT INTO centros_distribucion (id, nombre, ubicacion, codigo, capacidad, ocupacion) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, centro.getId());
            ps.setString(2, centro.getNombre());
            ps.setString(3, centro.getUbicacion());
            ps.setString(4, centro.getCodigo());
            ps.setInt(5, centro.getCapacidad());
            ps.setInt(6, centro.getOcupacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar centro: " + centro.getId(), e);
        }
    }

    @Override
    public void actualizar(CentroDistribucion centro) {
        String sql = "UPDATE centros_distribucion SET nombre=?, ubicacion=?, codigo=?, "
                   + "capacidad=?, ocupacion=? WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, centro.getNombre());
            ps.setString(2, centro.getUbicacion());
            ps.setString(3, centro.getCodigo());
            ps.setInt(4, centro.getCapacidad());
            ps.setInt(5, centro.getOcupacion());
            ps.setString(6, centro.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar centro: " + centro.getId(), e);
        }
    }

    @Override
    public void eliminar(String id) {
        String sql = "DELETE FROM centros_distribucion WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar centro: " + id, e);
        }
    }

    @Override
    public CentroDistribucion buscarPorId(String id) {
        String sql = "SELECT id, nombre, ubicacion, codigo, capacidad, ocupacion "
                   + "FROM centros_distribucion WHERE id=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new CentroDistribucion(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            rs.getString("ubicacion"),
                            rs.getString("codigo"),
                            rs.getInt("capacidad"),
                            rs.getInt("ocupacion")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar centro: " + id, e);
        }
    }
}
