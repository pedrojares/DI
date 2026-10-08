package com.clase.persistencia;
import java.util.List;

import com.clase.modelo.Paciente;

public interface  PacienteDAO {
    void guardarPaciente(Paciente paciente);
    List<Paciente> cargarPacientes();
//    Paciente buscarPaciente(String dni);
}
