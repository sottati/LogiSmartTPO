package com.logismart.infraestructura.persistencia.repositorio.sql;

import com.logismart.infraestructura.persistencia.entidad.CentroDistribucionEntity;
import com.logismart.infraestructura.persistencia.mapper.sql.CentroDistribucionMapperSQL;
import com.logismart.infraestructura.persistencia.repositorio.RepositorioCentro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion SQL de RepositorioCentro.
 * Delega en CentroDistribucionMapperSQL para las operaciones CRUD individuales.
 * Compila con JDK. No se ejecuta en tests.
 */
public class RepositorioCentroSQL implements RepositorioCentro {

    private final CentroDistribucionMapperSQL mapper;
    private final Connection connection;

    public RepositorioCentroSQL(Connection connection) {
        this.connection = connection;
        this.mapper = new CentroDistribucionMapperSQL(connection);
    }

    @Override
    public void guardar(CentroDistribucionEntity centro) {
        mapper.insertar(centro);
    }

    @Override
    public CentroDistribucionEntity obtener(int id) {
        return mapper.buscarPorId(String.valueOf(id));
    }

    @Override
    public List<CentroDistribucionEntity> obtenerTodos() {
        String sql = "SELECT id, nombre, ubicacion, codigo, capacidad, ocupacion "
                   + "FROM centros_distribucion";
        List<CentroDistribucionEntity> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(new CentroDistribucionEntity(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("ubicacion"),
                        rs.getString("codigo"),
                        rs.getInt("capacidad"),
                        rs.getInt("ocupacion")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todos los centros", e);
        }
        return lista;
    }

    @Override
    public void eliminar(int id) {
        mapper.eliminar(String.valueOf(id));
    }

    @Override
    public List<CentroDistribucionEntity> buscarPorUbicacion(String ubicacion) {
        String sql = "SELECT id, nombre, ubicacion, codigo, capacidad, ocupacion "
                   + "FROM centros_distribucion WHERE ubicacion=?";
        List<CentroDistribucionEntity> lista = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, ubicacion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new CentroDistribucionEntity(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            rs.getString("ubicacion"),
                            rs.getString("codigo"),
                            rs.getInt("capacidad"),
                            rs.getInt("ocupacion")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar centros por ubicacion: " + ubicacion, e);
        }
        return lista;
    }
}
