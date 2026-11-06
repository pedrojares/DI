package com.clase;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.clase.modelo.Doctor;
import com.clase.persistencia.DoctorDAOMySQL;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;



public class DoctoresController implements Initializable{

    @FXML 
    private TextField iddoc, apeldoc, nomdoc, movildoc, maildoc;
    @FXML 
    private ComboBox<String> espedoc;
    @FXML 
    private RadioButton coledocsi, coledocno;
    @FXML  
    private Button btnguardardoc, btnlimpiar, btnBuscardoc, btndeldoc;
    @FXML 
    private DatePicker nacpac;
    @FXML 
    private TableView<Doctor> tablaDoctores;
    @FXML 
    private TableColumn<Doctor, String> coliddoc;
    @FXML 
    private TableColumn<Doctor, String> colapeldoc;
    @FXML 
    private TableColumn<Doctor, String> colnomdoc;
    @FXML 
    private TableColumn<Doctor, String> colmovildoc;
    @FXML 
    private TableColumn<Doctor, String> colespedoc;
    
    private CitasController citasController;

    boolean doctorExiste = false;

    public void setCitasController(CitasController citasController) {
        this.citasController = citasController;
    }

    
    @Override 
    public void initialize(URL url, ResourceBundle rb){


        

        // Comprobar DNI al perder el foco
        iddoc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                
            }
        });
        
        //Comprobar mayúsculas nombre
        nomdoc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String nombre= letrasCapitales(nomdoc.getText());
                nomdoc  .setText(nombre);
            }
        });

        //Comprobar mayusculas apellido
        apeldoc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String apellido= letrasCapitales(apeldoc.getText());
                apeldoc.setText(apellido);
            }
        });

        //Comprobar movil
        movildoc.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comprobarMovil();
            }
        });


        ToggleGroup colegiadoGroup = new ToggleGroup();
        coledocsi.setToggleGroup(colegiadoGroup);
        coledocno.setToggleGroup(colegiadoGroup);

        try {
            cargarDoctores();
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los doctores.", e);
        }
        cargarEspecialidades();

        coliddoc.setCellValueFactory(new PropertyValueFactory<>("id"));
        colapeldoc.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colnomdoc.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colmovildoc.setCellValueFactory(new PropertyValueFactory<>("movil"));
        colespedoc.setCellValueFactory(new PropertyValueFactory<>("especialidad")); 
      
        tablaDoctores.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, anterior, nuevo) -> {
            
            if(nuevo != null){
                cargarDoctorEnFormulario(nuevo);
            }
        });
    }

    

    private void cargarEspecialidades() {

        // Abrimos el fichero JSON que está dentro de resources
        // usamos la clase de java InputStrem que lee datos en este caso de un fichero

        InputStream is = getClass()
                .getResourceAsStream("/com/clase/data/especialidades.json");

 
        // Leemos el JSON y lo convertimos en un objeto JsonObject
        JsonObject json = JsonParser.parseReader(
                    new InputStreamReader(is)
            ).getAsJsonObject();

 
        // Obtenemos el array "especialidades" del JSON
        JsonArray especialidades = json.getAsJsonArray("especialidades");

        // Recorremos todas las especialidades
        for (var especialidad : especialidades) {

            // Cada elemento del array es un objeto JSON
            JsonObject p = especialidad.getAsJsonObject();

            // Obtenemos el nombre de la especialidad
            // y lo añadimos al ComboBox
            espedoc.getItems().add(
                    p.get("nombre").getAsString()
            );
        }

    }
    
 
    
    

    //Letras capitalates
    private String letrasCapitales(String texto) {
        String[] palabras = texto.toLowerCase().trim().split("\\s+");
        StringBuilder resultado = new StringBuilder();

        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)))
                         .append(palabra.substring(1))
                         .append(" ");
            }
        }

        return resultado.toString().trim();
    }

    private void comprobarMovil(){
        String movil =movildoc.getText().trim();
        if(movil.isEmpty()){
            return;
        }
        if(validarMovil(movil)){
            movildoc.setStyle("");
            movildoc.setText(movil);
        }else{
            movildoc.setStyle("-fx-border-color: red;");
            movildoc.setText("");
        }
    }


    private boolean validarMovil(String telefono) {
        return telefono.matches("[67][0-9]{8}");
    }


    


    @FXML 
    private void guardarDoctor(){

        String id=iddoc.getText();        
        String apellidos=apeldoc.getText();
        String nombre=nomdoc.getText();
        String movil=movildoc.getText();
        String email=maildoc.getText();
        String especialidad=espedoc.getValue();
        boolean colegiado=coledocsi.isSelected();
        
        if (apellidos.trim().isEmpty() || nombre.trim().isEmpty() || movil.trim().isEmpty()
                || email.trim().isEmpty() || especialidad == null || especialidad.trim().isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Completa todos los campos del doctor antes de guardar.").showAndWait();
            return;
        }

        Doctor doctor = new Doctor(id, apellidos, nombre, movil, email, especialidad, colegiado);

        DoctorDAOMySQL dao = new DoctorDAOMySQL();
        if (doctorExiste) {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Confirmación");
            confirmation.setHeaderText(null);
            confirmation.setContentText("¿Está seguro de que desea modificar el doctor?");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return;
            }

            try {
                int updated = dao.modificarDoctor(doctor.getId(), doctor);
                if (updated == 0) {
                    new Alert(Alert.AlertType.INFORMATION, "No se modificó ningún doctor.").showAndWait();
                } else {
                    new Alert(Alert.AlertType.INFORMATION, "Doctor modificado correctamente.").showAndWait();
                }
            } catch (SQLException e) {
                mostrarError("No se pudo modificar el doctor.", e);
                return;
            }
        } else {
            Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
            confirmation.setTitle("Información");
            confirmation.setHeaderText(null);
            confirmation.setContentText("¿Desea guardar el doctor?");
            Optional<ButtonType> result = confirmation.showAndWait();
            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return;
            }

            try {
                dao.guardarDoctor(doctor);
                new Alert(Alert.AlertType.INFORMATION, "Doctor guardado correctamente.").showAndWait();
            } catch (SQLException e) {
                mostrarError("No se pudo guardar el doctor.", e);
                return;
            }
        }

        limpiarFormulario();
        try {
            cargarDoctores();
        } catch (SQLException e) {
            mostrarError("El doctor se guardó, pero no se pudo actualizar la tabla.", e);
        }
    }

    private void cargarDoctores() throws SQLException {
        DoctorDAOMySQL dao = new DoctorDAOMySQL();
        List<Doctor> doctores = dao.cargarDoctores();
        tablaDoctores.getItems().setAll(doctores);

    }

    @FXML
    private void limpiarFormulario() {

        iddoc.clear();
        apeldoc.clear();
        nomdoc.clear();
        movildoc.clear();
        maildoc.clear();
        coledocsi.setSelected(false);
        coledocno.setSelected(false);
        espedoc.setValue(null);
        movildoc.setStyle("");
        doctorExiste = false;
        if (citasController != null) {
            citasController.cargarDatosDoctor(null);
        }
    }

    @FXML
    private void eliminarDoctor(){

    Doctor seleccionado = tablaDoctores
                .getSelectionModel()
                .getSelectedItem();

        if(seleccionado == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("¿Desea eliminar el doctor?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    int deleted = new DoctorDAOMySQL().eliminarDoctor(seleccionado.getId());
                    if (deleted == 0) {
                        new Alert(Alert.AlertType.INFORMATION, "No se encontró el doctor para eliminar.").showAndWait();
                    } else {
                        new Alert(Alert.AlertType.INFORMATION, "Doctor eliminado correctamente.").showAndWait();
                    }
                    cargarDoctores();
                    limpiarFormulario();
                } catch (SQLException e) {
                    mostrarError("No se pudo eliminar el doctor.", e);
                }
            } else {
                return;
            }
    }




    @FXML 
    private void buscarDoctor(){
        String especialidad = espedoc.getValue();
        if (especialidad == null || especialidad.trim().isEmpty()) {
            try {
                cargarDoctores();
            } catch (SQLException e) {
                mostrarError("No se pudieron cargar los doctores.", e);
            }
            return;
        }

        List<Doctor> doctores;
        try {
            doctores = new DoctorDAOMySQL().buscarDoctoresPorEspecialidad(especialidad);
        } catch (SQLException e) {
            mostrarError("No se pudieron buscar doctores por especialidad.", e);
            return;
        }
        tablaDoctores.getItems().setAll(doctores);

        if (doctores.isEmpty()) {
            limpiarFormulario();
            new Alert(Alert.AlertType.INFORMATION, "No hay doctores para esa especialidad.").showAndWait();
            return;
        }

        Doctor doctor = doctores.get(0);
        cargarDoctorEnFormulario(doctor);
    }

    private void cargarDoctorEnFormulario(Doctor doctor) {
        doctorExiste = true;
        iddoc.setText(doctor.getId());
        apeldoc.setText(doctor.getApellidos());
        nomdoc.setText(doctor.getNombre());
        movildoc.setText(doctor.getMovil());
        maildoc.setText(doctor.getEmail());
        espedoc.setValue(doctor.getEspecialidad());
        coledocsi.setSelected(doctor.getColegiado());
        coledocno.setSelected(!doctor.getColegiado());

        if (citasController != null) {
            citasController.cargarDatosDoctor(doctor);
        }
    }

    private void mostrarError(String mensaje, SQLException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de base de datos");
        alert.setHeaderText(mensaje);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

}