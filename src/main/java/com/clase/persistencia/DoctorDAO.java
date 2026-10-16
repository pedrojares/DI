package com.clase.persistencia;
import java.util.List;

import com.clase.modelo.Doctor;

public interface  DoctorDAO {
    void guardarDoctor(Doctor doctor);
    List<Doctor> cargarDoctores();
    List<Doctor> buscarDoctoresPorEspecialidad(String especialidad);
    void eliminarDoctor(String id);
    void modificarDoctor(String id, Doctor doctor);
    
}
