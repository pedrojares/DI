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
    public void guardarPaciente(Paciente paciente){
        
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

                System.out.println("Paciente guardado correctamente");

            }catch(SQLException e){
                System.out.println("Error al guardar el paciente: "+ e.getMessage());

            }


    }
    // seleccionar pacientes de la bbdd
     @Override
    public List<Paciente> cargarPacientes() {

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
                        System.out.println(paciente);

                pacientes.add(paciente);
                System.out.println("hola");
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar los pacientes: " + e.getMessage());
        }
        //devuelve los pacientes que hay en la bbdd
        return pacientes;
    }
    
}
