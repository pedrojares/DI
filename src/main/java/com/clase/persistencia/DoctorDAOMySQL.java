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
    public void guardarDoctor(Doctor doctor){
        
        String sql = "INSERT INTO doctores"
            + "(iddoc, apeldoc, nomdoc, movildoc, maildoc, nacdoc, "
            + " dirdoc, prodoc, muidoc) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection conexion = ConexionMySQL.getConexion();
            PreparedStatement ps= conexion.prepareStatement(sql)){

                ps.setString(1, doctor.getId());
                ps.setString(2, doctor.getApellidos());
                ps.setString(3, doctor.getNombre());
                ps.setString(4, doctor.getMovil());
                ps.setString(5, doctor.getEmail());
                


                ps.executeUpdate();

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
                + "maildoc, nacdoc, dirdoc, prodoc, muidoc "
                + "FROM doctores "
                + "ORDER BY apeldoc, nomdoc     ";

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
                    false // coledoc    
                );

                doctores.add(doctor);
                System.out.println("hola");
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar los doctores: " + e.getMessage());
        }
        //devuelve los doctores que hay en la bbdd
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

    // Buscar un doctor por ID
    public Doctor buscarDoctor(String id) {

        String sql = "SELECT iddoc, apeldoc, nomdoc, movildoc, "
                + " maildoc, nacdoc, dirdoc, prodoc, muidoc "
                + " FROM doctores "
                + " WHERE iddoc = ?";

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
                        rs.getString("maildoc"),
                        rs.getString("espedoc"),
                        false // coledoc    
                    );

                    return doctor;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el doctor: " + e.getMessage());
        }

        return null;
    }

    
    public void modificarDoctor(String id, Doctor doctor) {

        String sql = "UPDATE doctores SET "
                + "apeldoc = ?, "
                + "nomdoc = ?, "
                + "movildoc = ?, "
                + "maildoc = ?, "
                + "nacdoc = ?, "
                + "dirdoc = ?, "
                + "prodoc = ?, "
                + "muidoc = ? "
                + "WHERE iddoc = ?";

        try (Connection conexion = ConexionMySQL.getConexion();
                PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, doctor.getApellidos());
            ps.setString(2, doctor.getNombre());
            ps.setString(3, doctor.getMovil());
            ps.setString(4, doctor.getEmail());
            

            // ID original para localizar el doctor
            ps.setString(9, id );

            ps.executeUpdate();

            System.out.println("Doctor modificado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al modificar el doctor: " + e.getMessage());
        }
    }

     // Buscar un doctor por ID
    public Doctor buscaDoctor(String id) {

        String sql = "SELECT iddoc, apeldoc, nomdoc, movildoc, "
                + " maildoc, nacdoc, dirdoc, prodoc, muidoc "
                + " FROM doctores "
                + " WHERE iddoc = ?";

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
                        rs.getString("maildoc"),
                        rs.getString("espedoc"),
                        false // coledoc
                    );
                    return doctor;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar el doctor: " + e.getMessage());
        }

        return null;
    }
    
}
