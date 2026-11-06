package com.clase.persistencia;

import java.sql.SQLException;
import java.util.List;

import com.clase.modelo.Doctor;

public interface  DoctorDAO {
    void guardarDoctor(Doctor doctor) throws SQLException;
    List<Doctor> cargarDoctores() throws SQLException;
    List<Doctor> buscarDoctoresPorEspecialidad(String especialidad) throws SQLException;
    int eliminarDoctor(String id) throws SQLException;
    int modificarDoctor(String id, Doctor doctor) throws SQLException;
    
}
