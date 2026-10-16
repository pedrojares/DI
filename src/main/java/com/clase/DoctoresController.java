package com.clase;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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
    

    boolean doctorExiste = false;

    
    
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


        cargarDoctores();
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
        

        System.out.println("----------------DOCTOR-------------");
        System.out.println("ID: "+id);
        System.out.println("Apellidos: "+apellidos);
        System.out.println("Nombre: "+nombre);
        System.out.println("Telefono: "+movil);
        System.out.println("Email: "+email);
        System.out.println("Especialidad: "+especialidad);
        System.out.println("Colegiado: "+colegiado);
        System.out.println("--------------------------------------");

        Doctor doctor = new Doctor(id, apellidos, nombre, movil, email, especialidad, colegiado);

        if(doctorExiste){
            DoctorDAOMySQL dao = new DoctorDAOMySQL();
            dao.modificarDoctor(doctor.getId(), doctor);
            doctorExiste = false;
            

        }else{
            DoctorDAOMySQL dao = new DoctorDAOMySQL();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("¿Desea guardar el doctor?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                dao.guardarDoctor(doctor);
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Información");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Doctor guardado correctamente.");
                infoAlert.showAndWait();
            } else {
                return;
            }
            

        }
        
        cargarDoctores();
    }

    @FXML 
    private void cargarDoctores(){

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
    }

    @FXML
    private void eliminarDoctor(){

    Doctor seleccionado = tablaDoctores
                .getSelectionModel()
                .getSelectedItem();

        if(seleccionado == null){
            return;
        }
        DoctorDAOMySQL dao = new DoctorDAOMySQL();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("¿Desea eliminar el doctor?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                dao.eliminarDoctor(seleccionado.getId());
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Información");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Doctor eliminado correctamente.");
                infoAlert.showAndWait();
            } else {
                return;
            }

        //Recargamos la tabla de doctores
        cargarDoctores();
    
    }




    @FXML 
    private void buscarDoctor(){
        String especialidad = espedoc.getValue();
        if (especialidad == null || especialidad.trim().isEmpty()) {
            cargarDoctores();
            return;
        }

        DoctorDAOMySQL dao = new DoctorDAOMySQL();
        List<Doctor> doctores = dao.buscarDoctoresPorEspecialidad(especialidad);
        tablaDoctores.getItems().setAll(doctores);

        if (doctores.isEmpty()) {
            doctorExiste = false;
            iddoc.clear();
            apeldoc.clear();
            nomdoc.clear();
            movildoc.clear();
            maildoc.clear();
            coledocsi.setSelected(false);
            coledocno.setSelected(false);
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
    }

}