package com.clase.persistencia;

import java.sql.SQLException;
import java.util.List;

import com.clase.modelo.Paciente;

public interface  PacienteDAO {
    void guardarPaciente(Paciente paciente) throws SQLException;
    List<Paciente> cargarPacientes() throws SQLException;
    int eliminarPaciente(String dni) throws SQLException;
    Paciente buscarPaciente(String dni) throws SQLException;
    int modificarPaciente(String dni, Paciente paciente) throws SQLException;
    Paciente buscaPacdni(String dni) throws SQLException;
}
