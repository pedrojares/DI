package com.clase.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clase.modelo.Paciente;

public class PacienteDAOMySQL implements PacienteDAO{

    @Override 
    public void guardarPaciente(Paciente paciente) throws SQLException {
        
        String sql = "INSERT INTO pacientes"
            + "(dnipac, apelpac, nompac, movilpac, emailpac, nacpac, "
            + " dirpac, propac, munipac) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps= conexion.prepareStatement(sql)){

                ps.setString(1, paciente.getDni());
                ps.setString(2, paciente.getApellidos());
                ps.setString(3, paciente.getNombre());
                ps.setString(4, paciente.getMovil());
                ps.setString(5, paciente.getEmail());
                ps.setDate(6, java.sql.Date.valueOf(paciente.getNacimiento()));
                ps.setString(7, paciente.getDireccion());
                ps.setString(8, paciente.getProvincia());
                ps.setString(9, paciente.getMunicipio());

                ps.executeUpdate();
        }
    }
    // seleccionar pacientes de la bbdd
     @Override
    public List<Paciente> cargarPacientes() throws SQLException {

        List<Paciente> pacientes = new ArrayList<>();

        // Solo obtenemos los campos que necesitamos para la tabla
        String sql = "SELECT dnipac, apelpac, nompac, movilpac, "
                + "propac, munipac "
                + "FROM pacientes "
                + "ORDER BY apelpac, nompac";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            // Recorremos las filas obtenidas
            while (rs.next()) {

                Paciente paciente = new Paciente(
                        rs.getString("dnipac"),
                        rs.getString("apelpac"),
                        rs.getString("nompac"),
                        rs.getString("movilpac"),
                        rs.getString("propac"),
                        rs.getString("munipac"));

                pacientes.add(paciente);
            }

        }
        return pacientes;
    }

    public int eliminarPaciente(String dni) throws SQLException {

        String sql = "DELETE FROM pacientes WHERE dnipac = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)){
                
                ps.setString(1, dni);

                return ps.executeUpdate();
            } 
    }

    // Buscar un paciente por DNI
    public Paciente buscarPaciente(String dni) throws SQLException {

        String sql = "SELECT dnipac, apelpac, nompac, movilpac, "
                + " emailpac, nacpac, dirpac, propac, munipac "
                + " FROM pacientes "
                + " WHERE dnipac = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Paciente paciente = new Paciente(
                            rs.getString("dnipac"),
                            rs.getString("apelpac"),
                            rs.getString("nompac"),
                            rs.getString("movilpac"),
                            rs.getString("emailpac"),
                            rs.getDate("nacpac").toLocalDate(),
                            rs.getString("dirpac"),
                            rs.getString("propac"),
                            rs.getString("munipac"));

                    return paciente;
                }
            }

        }
        return null;
    }

    
    public int modificarPaciente(String dni, Paciente paciente) throws SQLException {

        String sql = "UPDATE pacientes SET "
                + "apelpac = ?, "
                + "nompac = ?, "
                + "movilpac = ?, "
                + "emailpac = ?, "
                + "nacpac = ?, "
                + "dirpac = ?, "
                + "propac = ?, "
                + "munipac = ? "
                + "WHERE dnipac = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, paciente.getApellidos());
            ps.setString(2, paciente.getNombre());
            ps.setString(3, paciente.getMovil());
            ps.setString(4, paciente.getEmail());
            ps.setDate(5, java.sql.Date.valueOf(paciente.getNacimiento()));
            ps.setString(6, paciente.getDireccion());
            ps.setString(7, paciente.getProvincia());
            ps.setString(8, paciente.getMunicipio());

            // DNI original para localizar el paciente
            ps.setString(9, dni);

            return ps.executeUpdate();
        }
    }

     // Buscar un paciente por DNI
    public Paciente buscaPacdni(String dni) throws SQLException {

        String sql = "SELECT dnipac, apelpac, nompac, movilpac, "
                + " emailpac, nacpac, dirpac, propac, munipac "
                + " FROM pacientes "
                + " WHERE dnipac = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, dni);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Paciente paciente = new Paciente(
                            rs.getString("dnipac"),
                            rs.getString("apelpac"),
                            rs.getString("nompac"),
                            rs.getString("movilpac"),
                            rs.getString("emailpac"),
                            rs.getDate("nacpac").toLocalDate(),
                            rs.getString("dirpac"),
                            rs.getString("propac"),
                            rs.getString("munipac"));

                    return paciente;
                }
            }

        }
        return null;
    }
    
}
