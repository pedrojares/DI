package com.clase.persistencia;
import java.util.List;

import com.clase.modelo.Paciente;

public interface  PacienteDAO {
    void guardarPaciente(Paciente paciente);
    List<Paciente> cargarPacientes();
    void eliminarPaciente(String dni);
    Paciente buscarPaciente(String dni);
    void modificarPaciente(String dni, Paciente paciente);
    Paciente buscaPacdni(String dni);
}
