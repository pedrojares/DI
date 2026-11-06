
package com.clase;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

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

    public void cargarDatosPaciente(Paciente paciente) {
        pacienteSeleccionado = paciente;
        idDoctor.clear();
        citadnipac.clear();
        limpiarDatosCita();

        if (paciente == null) {
            return;
        }

        citadnipac.setText(paciente.getDni());
    }

    public void cargarDatosDoctor(Doctor doctor) {
        idDoctor.clear();
        if (doctor == null) {
            limpiarDatosCita();
            return;
        }

        idDoctor.setText(doctor.getId());
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
        idCita.setText(String.valueOf(cita.getIdcita()));
        idDoctor.setText(String.valueOf(cita.getCitaiddoc()));
        chkpendiente.setSelected("Pendiente".equalsIgnoreCase(cita.getEstadocita()));
        chkatendida.setSelected("Atendida".equalsIgnoreCase(cita.getEstadocita()));
        chkanulada.setSelected("Anulada".equalsIgnoreCase(cita.getEstadocita()));
    }

    private void limpiarDatosCita() {
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
