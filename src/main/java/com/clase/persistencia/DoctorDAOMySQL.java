package com.clase.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.clase.modelo.Doctor;

public class DoctorDAOMySQL implements DoctorDAO{

    @Override 
    public void guardarDoctor(Doctor doctor) throws SQLException {
        
        String sql = "INSERT INTO doctores"
            + "(apeldoc, nomdoc, movildoc, emaildoc, espedoc, coledoc) "
            + "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)){

                ps.setString(1, doctor.getApellidos());
                ps.setString(2, doctor.getNombre());
                ps.setString(3, doctor.getMovil());
                ps.setString(4, doctor.getEmail());
                ps.setString(5, doctor.getEspecialidad());
                ps.setBoolean(6, doctor.getColegiado());

                ps.executeUpdate();

                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        doctor.setId(generatedKeys.getString(1));
                    }
                }
            }
    }
    // seleccionar doctores de la bbdd
     @Override
    public List<Doctor> cargarDoctores() throws SQLException {

        List<Doctor> doctores = new ArrayList<>();

        // Solo obtenemos los campos que necesitamos para la tabla
        String sql = "SELECT iddoc, apeldoc, nomdoc, movildoc, "
                + "emaildoc, espedoc, coledoc "
                + "FROM doctores "
                + "ORDER BY apeldoc, nomdoc";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            // Recorremos las filas obtenidas
            while (rs.next()) {

                Doctor doctor = new Doctor(
                    rs.getString("iddoc"),
                    rs.getString("apeldoc"),
                    rs.getString("nomdoc"),
                    rs.getString("movildoc"),
                    rs.getString("emaildoc"),
                    rs.getString("espedoc"),
                    rs.getBoolean("coledoc")
                );

                doctores.add(doctor);
            }

        }
        return doctores;
    }

    @Override
    public List<Doctor> buscarDoctoresPorEspecialidad(String especialidad) throws SQLException {

        List<Doctor> doctores = new ArrayList<>();
        String sql = "SELECT iddoc, apeldoc, nomdoc, movildoc, "
                + "emaildoc, espedoc, coledoc "
                + "FROM doctores "
                + "WHERE espedoc = ? "
                + "ORDER BY apeldoc, nomdoc";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, especialidad);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Doctor doctor = new Doctor(
                            rs.getString("iddoc"),
                            rs.getString("apeldoc"),
                            rs.getString("nomdoc"),
                            rs.getString("movildoc"),
                            rs.getString("emaildoc"),
                            rs.getString("espedoc"),
                            rs.getBoolean("coledoc"));
                    doctores.add(doctor);
                }
            }
        }

        return doctores;
    }

    @Override
    public int eliminarDoctor(String id) throws SQLException {

        String sql = "DELETE FROM doctores WHERE iddoc = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)){
                
                ps.setString(1, id);
                return ps.executeUpdate();
            } 
    }

    

    
    @Override
    public int modificarDoctor(String id, Doctor doctor) throws SQLException {

        String sql = "UPDATE doctores SET "
                + "apeldoc = ?, "
                + "nomdoc = ?, "
                + "movildoc = ?, "
                + "emaildoc = ?, "
                + "espedoc = ?, "
                + "coledoc = ? "
                + "WHERE iddoc = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, doctor.getApellidos());
            ps.setString(2, doctor.getNombre());
            ps.setString(3, doctor.getMovil());
            ps.setString(4, doctor.getEmail());
            ps.setString(5, doctor.getEspecialidad());
            ps.setBoolean(6, doctor.getColegiado());
            ps.setString(7, id);

            return ps.executeUpdate();
        }
    }

     // Buscar un doctor por ID
    public Doctor buscaDoctor(String id) {

        String sql = "SELECT iddoc, apeldoc, nomdoc, movildoc, "
                + "emaildoc, espedoc, coledoc "
                + "FROM doctores "
                + "WHERE iddoc = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Doctor doctor = new Doctor(
                        rs.getString("iddoc"),
                        rs.getString("apeldoc"),
                        rs.getString("nomdoc"),
                        rs.getString("movildoc"),
                        rs.getString("espedoc"),
                        rs.getBoolean("coledoc")
                    );
                    doctor.setEmail(rs.getString("emaildoc"));
                    return doctor;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el doctor: " + e.getMessage());
        }

        return null;
    }


    public List<Doctor> cargarDoctoresEspec(String espedoc) {

        List<Doctor> doctores = new ArrayList<>();
        String sql = "SELECT iddoc, apeldoc, nomdoc, movildoc, "
                + "emaildoc, espedoc, coledoc "
                + "FROM doctores "
                + "WHERE espedoc = ? "
                + "ORDER BY apeldoc, nomdoc";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, espedoc);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Doctor doctor = new Doctor(
                            rs.getString("iddoc"),
                            rs.getString("apeldoc"),
                            rs.getString("nomdoc"),
                            rs.getString("movildoc"),
                            rs.getString("emaildoc"),
                            rs.getString("espedoc"),
                            rs.getBoolean("coledoc"));
                    doctores.add(doctor);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar doctores por especialidad: " + e.getMessage());
        }

        return doctores;

    }




    
    
}
