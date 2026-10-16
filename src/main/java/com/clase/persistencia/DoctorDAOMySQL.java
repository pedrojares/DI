package com.clase.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.clase.modelo.Doctor;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DoctorDAOMySQL implements DoctorDAO{

    @Override 
    public void guardarDoctor(Doctor doctor){
        
        String sql = "INSERT INTO doctores"
            + "(apeldoc, nomdoc, movildoc, maildoc, espedoc, coledoc) "
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

                System.out.println("Doctor guardado correctamente");

            }catch(SQLException e){
                System.out.println("Error al guardar el doctor: "+ e.getMessage());

            }


    }
    // seleccionar doctores de la bbdd
     @Override
    public List<Doctor> cargarDoctores() {

        List<Doctor> doctores = new ArrayList<>();

        // Solo obtenemos los campos que necesitamos para la tabla
        String sql = "SELECT iddoc, apeldoc, nomdoc, movildoc, "
                + "maildoc, espedoc, coledoc "
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
                    rs.getString("maildoc"),
                    rs.getString("espedoc"),
                    rs.getBoolean("coledoc")
                );

                doctores.add(doctor);
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar los doctores: " + e.getMessage());
        }
        //devuelve los doctores que hay en la bbdd
        return doctores;
    }

    @Override
    public List<Doctor> buscarDoctoresPorEspecialidad(String especialidad) {

        List<Doctor> doctores = new ArrayList<>();
        String sql = "SELECT iddoc, apeldoc, nomdoc, movildoc, "
                + "maildoc, espedoc, coledoc "
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
                            rs.getString("maildoc"),
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

    public void eliminarDoctor(String id){

        String sql = "DELETE FROM doctores WHERE iddoc = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps = conexion.prepareStatement(sql)){
                
                ps.setString(1, id);

                ps.executeUpdate();

                System.out.println("Doctor eliminado correctamente.");
            } catch(SQLException e){
                System.out.println("Error al eliminar el doctor." + e.getMessage());
            } 
    

    }

    

    
    public void modificarDoctor(String id, Doctor doctor) {

        String sql = "UPDATE doctores SET "
                + "apeldoc = ?, "
                + "nomdoc = ?, "
                + "movildoc = ?, "
                + "maildoc = ?, "
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

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmación");
            alert.setHeaderText(null);
            alert.setContentText("¿Está seguro de que desea modificar el doctor?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                ps.executeUpdate();
                System.out.println("Doctor modificado correctamente.");
            }else {
                return;
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar el doctor: " + e.getMessage());
        }
    }

     // Buscar un doctor por ID
    public Doctor buscaDoctor(String id) {

        String sql = "SELECT iddoc, apeldoc, nomdoc, movildoc, "
                + "maildoc, espedoc, coledoc "
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
                    doctor.setEmail(rs.getString("maildoc"));
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
                + "maildoc, espedoc, coledoc "
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
                            rs.getString("maildoc"),
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
