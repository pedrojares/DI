
package com.clase;

import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.clase.modelo.Cita;
import com.clase.modelo.Doctor;
import com.clase.modelo.Paciente;
import com.clase.persistencia.CitaDAOMySQL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CitasController implements Initializable {

    @FXML
    private TextField idCita, idDoctor, citadnipac;

    @FXML
    private CheckBox chkatendida, chkpendiente, chkanulada;

    @FXML
    private TextField motivoCita;

    @FXML
    private DatePicker fechaCita;

    @FXML
    private ComboBox<String> horaCita;

    @FXML
    private TextArea diagnosticoCita, tratamientoCita;

    @FXML
    private TableView<Cita> tablaCitas;

    @FXML
    private TableColumn<Cita, Integer> colIdCita;

    @FXML
    private TableColumn<Cita, String> colDoctor, colEspecialidad, colEstado;

    @FXML
    private TableColumn<Cita, java.time.LocalDate> colDia;

    @FXML
    private TableColumn<Cita, java.time.LocalTime> colHora;

    private Paciente pacienteSeleccionado;
    private String doctorSeleccionadoId;
    private Integer citaSeleccionadaId;

    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    public void cargarDatosPaciente(Paciente paciente) {
        pacienteSeleccionado = paciente;
        idDoctor.clear();
        doctorSeleccionadoId = null;
        citadnipac.clear();
        limpiarDatosCita();

        if (paciente == null) {
            return;
        }

        citadnipac.setText(paciente.getDni());
    }

    public void cargarDatosDoctor(Doctor doctor) {
        idDoctor.clear();
        citaSeleccionadaId = null;
        limpiarDatosCita();
        if (doctor == null) {
            doctorSeleccionadoId = null;
            return;
        }

        idDoctor.setText(doctor.getId());
        doctorSeleccionadoId = doctor.getId();
        actualizarHorasDisponibles();
        cargarCitasPacienteSeleccionado();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCita.setEditable(false);
        idDoctor.setEditable(false);
        colIdCita.setCellValueFactory(new PropertyValueFactory<>("idcita"));
        colDoctor.setCellValueFactory(new PropertyValueFactory<>("doctor"));
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidad"));
        colDia.setCellValueFactory(new PropertyValueFactory<>("fechacita"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("horacita"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estadocita"));
        fechaCita.valueProperty().addListener((observable, anterior, fecha) -> actualizarHorasDisponibles());

        tablaCitas.getSelectionModel().selectedItemProperty().addListener((observable, anterior, cita) -> {
            if (cita != null) {
                cargarCita(cita);
            }
        });
    }

    private void cargarCitasPacienteSeleccionado() {
        tablaCitas.getItems().clear();
        if (pacienteSeleccionado == null) {
            return;
        }

        try {
            tablaCitas.getItems().setAll(
                    new CitaDAOMySQL().cargarCitasPaciente(pacienteSeleccionado.getDni()));
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al cargar citas");
            alert.setHeaderText("No se pudieron cargar las citas del paciente.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private void cargarCita(Cita cita) {
        citaSeleccionadaId = cita.getIdcita();
        doctorSeleccionadoId = String.valueOf(cita.getCitaiddoc());
        idCita.setText(String.valueOf(cita.getIdcita()));
        idDoctor.setText(String.valueOf(cita.getCitaiddoc()));
        citadnipac.setText(cita.getCitadnipac());
        motivoCita.setText(cita.getMotivo() == null ? "" : cita.getMotivo());
        fechaCita.setValue(cita.getFechacita());
        actualizarHorasDisponibles();
        horaCita.setValue(cita.getHoracita().format(FORMATO_HORA));
        diagnosticoCita.setText(cita.getDiagnostico() == null ? "" : cita.getDiagnostico());
        tratamientoCita.setText(cita.getTratamiento() == null ? "" : cita.getTratamiento());
        chkpendiente.setSelected("Pendiente".equalsIgnoreCase(cita.getEstadocita()));
        chkatendida.setSelected("Atendida".equalsIgnoreCase(cita.getEstadocita()));
        chkanulada.setSelected("Anulada".equalsIgnoreCase(cita.getEstadocita()));
    }

    private void limpiarDatosCita() {
        citaSeleccionadaId = null;
        idCita.clear();
        motivoCita.clear();
        fechaCita.setValue(null);
        horaCita.setValue(null);
        diagnosticoCita.clear();
        tratamientoCita.clear();
        chkpendiente.setSelected(false);
        chkatendida.setSelected(false);
        chkanulada.setSelected(false);
        tablaCitas.getItems().clear();
        tablaCitas.getSelectionModel().clearSelection();
        actualizarHorasDisponibles();
    }

    private void actualizarHorasDisponibles() {
        horaCita.getItems().clear();
        horaCita.setValue(null);

        LocalDate fecha = fechaCita.getValue();
        if (doctorSeleccionadoId == null || fecha == null
                || fecha.getDayOfWeek() == DayOfWeek.SATURDAY
                || fecha.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return;
        }

        try {
            Set<LocalTime> ocupadas = new CitaDAOMySQL().cargarHorasOcupadas(
                    doctorSeleccionadoId, fecha, citaSeleccionadaId);
            List<String> disponibles = new ArrayList<>();
            agregarHorasDisponibles(disponibles, LocalTime.of(9, 0), LocalTime.of(14, 0), ocupadas);
            agregarHorasDisponibles(disponibles, LocalTime.of(16, 0), LocalTime.of(20, 0), ocupadas);
            horaCita.getItems().setAll(disponibles);
        } catch (SQLException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al consultar disponibilidad");
            alert.setHeaderText("No se pudieron cargar las horas disponibles.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    private void guardarCita() {
        if (pacienteSeleccionado == null || doctorSeleccionadoId == null) {
            mostrarAviso("Selecciona primero un paciente y un doctor.");
            return;
        }
        if (fechaCita.getValue() == null || horaCita.getValue() == null) {
            mostrarAviso("Selecciona el día y una hora disponible.");
            return;
        }

        String estado = chkpendiente.isSelected() ? "Pendiente"
                : chkatendida.isSelected() ? "Atendida"
                : chkanulada.isSelected() ? "Anulada" : "Pendiente";
        boolean nuevaCita = citaSeleccionadaId == null;
        Cita cita = new Cita(
                nuevaCita ? 0 : citaSeleccionadaId,
                pacienteSeleccionado.getDni(),
                Integer.parseInt(doctorSeleccionadoId),
                fechaCita.getValue(),
                LocalTime.parse(horaCita.getValue(), FORMATO_HORA),
                valorOpcional(motivoCita.getText()),
                valorOpcional(diagnosticoCita.getText()),
                valorOpcional(tratamientoCita.getText()),
                estado);

        try {
            CitaDAOMySQL dao = new CitaDAOMySQL();
            if (nuevaCita) {
                cita.setIdcita(dao.guardarCita(cita));
            } else {
                dao.modificarCita(cita);
            }

            cargarCitasPacienteSeleccionado();
            for (Cita citaCargada : tablaCitas.getItems()) {
                if (citaCargada.getIdcita() == cita.getIdcita()) {
                    tablaCitas.getSelectionModel().select(citaCargada);
                    break;
                }
            }
            new Alert(Alert.AlertType.INFORMATION,
                    nuevaCita ? "Cita guardada correctamente."
                            : "Cita actualizada correctamente.")
                    .showAndWait();
        } catch (SQLException | NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al guardar cita");
            alert.setHeaderText("No se pudo guardar la cita.");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private String valorOpcional(String valor) {
        String valorLimpio = valor.trim();
        return valorLimpio.isEmpty() ? null : valorLimpio;
    }

    private void mostrarAviso(String mensaje) {
        new Alert(Alert.AlertType.WARNING, mensaje).showAndWait();
    }

    private void agregarHorasDisponibles(List<String> disponibles, LocalTime inicio,
            LocalTime fin, Set<LocalTime> ocupadas) {
        for (LocalTime hora = inicio; hora.isBefore(fin); hora = hora.plusMinutes(30)) {
            if (!ocupadas.contains(hora)) {
                disponibles.add(hora.format(FORMATO_HORA));
            }
        }
    }
    
    @FXML
    private void gestionarExclusion(ActionEvent event) {
        CheckBox seleccionado = (CheckBox) event.getSource();
        
        if (seleccionado == chkpendiente && chkpendiente.isSelected()) {
            chkatendida.setSelected(false);
            chkanulada.setSelected(false);
        } else if (seleccionado == chkatendida && chkatendida.isSelected()) {
            chkpendiente.setSelected(false);
            chkanulada.setSelected(false);
        } else if (seleccionado == chkanulada && chkanulada.isSelected()) {
            chkpendiente.setSelected(false);
            chkatendida.setSelected(false);
        }
    }
}
