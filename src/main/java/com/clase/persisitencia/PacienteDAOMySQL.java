package com.clase.persisitencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
    
}
