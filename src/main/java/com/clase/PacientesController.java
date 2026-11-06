package com.clase;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.clase.modelo.Paciente;
import com.clase.persistencia.PacienteDAOMySQL;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;



public class PacientesController implements Initializable{

    @FXML 
    private TextField dnipac, apelpac, nompac, movilpac, emailpac, dirpac;
    @FXML 
    private ComboBox<String> propac, munipac;
    @FXML  
    private Button btnguardarpac, btnlimpiar, btnBuscarPac, btndelpac;
    @FXML 
    private DatePicker nacpac;
    @FXML 
    private TableView<Paciente> tablaPacientes;
    @FXML 
    private TableColumn<Paciente, String> coldnipac;
    @FXML 
    private TableColumn<Paciente, String> colapelpac;
    @FXML 
    private TableColumn<Paciente, String> colnompac;
    @FXML 
    private TableColumn<Paciente, String> colmovilpac;
    @FXML 
    private TableColumn<Paciente, String> colpropac;
    @FXML 
    private TableColumn<Paciente, String> colmunipac;

    private CitasController citasController;
    private String dniPacienteOriginal;

    boolean pacienteExiste = false;

    public void setCitasController(CitasController citasController) {
        this.citasController = citasController;
    }

    
    
    @Override 
    public void initialize(URL url, ResourceBundle rb){


        

        // Comprobar DNI al perder el foco
        dnipac.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comprobarDni();
            }
        });
        
        //Comprobar mayúsculas nombre
        nompac.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String nombre= letrasCapitales(nompac.getText());
                nompac.setText(nombre);
            }
        });

        //Comprobar mayusculas apellido
        apelpac.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                String apellido= letrasCapitales(apelpac.getText());
                apelpac.setText(apellido);
            }
        });

        //Comprobar movil
        movilpac.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                comprobarMovil();
            }
        });

        cargarProvincias();

        propac.setOnAction(e -> cargarMunicipios());

        cargarPacientes();

        coldnipac.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colapelpac.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        colnompac.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colmovilpac.setCellValueFactory(new PropertyValueFactory<>("movil"));
        colpropac.setCellValueFactory(new PropertyValueFactory<>("provincia"));
        colmunipac.setCellValueFactory(new PropertyValueFactory<>("municipio")); 
      
        tablaPacientes.getSelectionModel()
        .selectedItemProperty()
        .addListener((observable, anterior, nuevo) -> {
            
            if(nuevo != null){
                cargarPaciente();
            }
        });
    }

    

    

    //Validar dni

    @FXML 
    private void comprobarDni(){

        String dni = dnipac.getText().trim().toUpperCase();

        if(dni.isEmpty())
            return;
            
        if(validarDniNie(dni)){
            dnipac.setStyle("");
        }else{
            dnipac.setStyle("-fx:border-color:red;");
            dnipac.setText("");
        }
    }
        
    
    
    private boolean validarDniNie(String documento) {

        if (documento.matches("\\d{8}[A-Z]")) {

            int numero = Integer.parseInt(documento.substring(0, 8));
            char letra = "TRWAGMYFPDXBNJZSQVHLCKE".charAt(numero % 23);

            return letra == documento.charAt(8);
        }

        if (documento.matches("[XYZ]\\d{7}[A-Z]")) {

            String nie = documento
                    .replace("X", "0")
                    .replace("Y", "1")
                    .replace("Z", "2");

            int numero = Integer.parseInt(nie.substring(0, 8));
            char letra = "TRWAGMYFPDXBNJZSQVHLCKE".charAt(numero % 23);

            return letra == documento.charAt(8);
        }

        return false;
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
        String movil =movilpac.getText().trim();
        if(movil.isEmpty()){
            return;
        }
        if(validarMovil(movil)){
            movilpac.setStyle("");
            movilpac.setText(movil);
        }else{
            movilpac.setStyle("-fx-border-color: red;");
            movilpac.setText("");
        }
    }


    private boolean validarMovil(String telefono) {
        return telefono.matches("[67][0-9]{8}");
    }

    private void cargarProvincias() {

        // Abrimos el fichero JSON que está dentro de resources
        // usamos la clase de java InputStrem que lee datos en este caso de un fichero

        InputStream is = getClass()
                .getResourceAsStream("/com/clase/data/municipios.json");

 
        // Leemos el JSON y lo convertimos en un objeto JsonObject
        JsonObject json = JsonParser.parseReader(
                new InputStreamReader(is)
        ).getAsJsonObject();

 
        // Obtenemos el array "provincias" del JSON
        JsonArray provincias = json.getAsJsonArray("provincias");

        // Recorremos todas las provincias
        for (var provincia : provincias) {

            // Cada elemento del array es un objeto JSON
            JsonObject p = provincia.getAsJsonObject();

            // Obtenemos el nombre de la provincia
            // y lo añadimos al ComboBox
            propac.getItems().add(
                    p.get("nm").getAsString()
            );
        }

    }
    
    private void cargarMunicipios() {

        // Abrimos de nuevo el fichero JSON
        InputStream is = getClass()
                .getResourceAsStream("/com/clase/data/municipios.json");

        // Convertimos el contenido del fichero en un JsonObject
        JsonObject json = JsonParser.parseReader(
                new InputStreamReader(is)
        ).getAsJsonObject();

        // Obtenemos los dos arrays que necesitamos
        JsonArray provincias = json.getAsJsonArray("provincias");
        JsonArray municipios = json.getAsJsonArray("municipios");

        // Obtenemos el nombre de la provincia seleccionada
        String nombreProvincia = propac.getValue();

        // Variable donde guardaremos el código de la provincia
        String idProvincia = "";

        // Recorremos las provincias
        for (var provincia : provincias) {

            JsonObject p = provincia.getAsJsonObject();

            // Comprobamos si es la provincia seleccionada
            if (p.get("nm").getAsString().equals(nombreProvincia)) {

                // Obtenemos su código
                idProvincia = p.get("id").getAsString();

                // Ya hemos encontrado la provincia
                break;
            }
        }

        // Eliminamos los municipios que pudiera haber
        // de una selección anterior muy importante sino agrega municipios
        munipac.getItems().clear();

        // Recorremos todos los municipios
        for (var municipio : municipios) {

            JsonObject m = municipio.getAsJsonObject();

            // Comprobamos los dos primeros caracteres del código
            if (m.get("id").getAsString().startsWith(idProvincia)) {

                // Si pertenecen a la provincia,
                // añadimos su nombre al ComboBox
                munipac.getItems().add(
                        m.get("nm").getAsString()
                );
            }
        }
    }


    @FXML 
    private void guardarPaciente(){

        String dni=dnipac.getText();        
        String apellidos=apelpac.getText();
        String nombre=nompac.getText();
        LocalDate fechaNacimiento=nacpac.getValue();
        String movil=movilpac.getText();
        String email=emailpac.getText();
        String direccion=dirpac.getText();
        String provincia=propac.getValue();
        String municipio=munipac.getValue();

        if (dni.trim().isEmpty() || apellidos.trim().isEmpty() || nombre.trim().isEmpty()
                || fechaNacimiento == null || movil.trim().isEmpty() || email.trim().isEmpty()
                || direccion.trim().isEmpty() || provincia == null || municipio == null) {
            new Alert(Alert.AlertType.WARNING, "Completa todos los campos del paciente antes de guardar.").showAndWait();
            return;
        }

        Paciente paciente = new Paciente(dni, apellidos, nombre, movil, email, fechaNacimiento, direccion, provincia, municipio);
        
        if(pacienteExiste){
            Alert alert3 = new Alert(Alert.AlertType.CONFIRMATION);
            alert3.setTitle("Aviso");
            alert3.setHeaderText(null);
            alert3.setContentText("¿Está seguro de que desea modificar el paciente?");
            Optional<ButtonType> result = alert3.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    int updated = new PacienteDAOMySQL().modificarPaciente(dniPacienteOriginal, paciente);
                    String mensaje = updated == 0
                            ? "No se modificó ningún paciente."
                            : "Paciente modificado correctamente.";
                    new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
                } catch (SQLException e) {
                    mostrarError("No se pudo modificar el paciente.", e);
                    return;
                }
            } else {
                return;
            }

        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("Desea guardar el paciente?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    new PacienteDAOMySQL().guardarPaciente(paciente);
                    new Alert(Alert.AlertType.INFORMATION, "Paciente guardado correctamente.").showAndWait();
                } catch (SQLException e) {
                    mostrarError("No se pudo guardar el paciente.", e);
                    return;
                }
            } else {
                return;
            }
            
        }
        
        cargarPacientes();
        limpiarFormulario();
    }

    private void cargarPacientes() {
        try {
            List<Paciente> pacientes = new PacienteDAOMySQL().cargarPacientes();
            tablaPacientes.getItems().setAll(pacientes);
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los pacientes.", e);
        }
    }

    @FXML
    private void limpiarFormulario() {

        dnipac.clear();
        apelpac.clear();
        nompac.clear();
        movilpac.clear();
        emailpac.clear();
        dirpac.clear();

        nacpac.setValue(null);

        propac.setValue(null);
        munipac.setValue(null);

        dnipac.setStyle("");
        dnipac.setEditable(true);
        movilpac.setStyle("");
        pacienteExiste = false;
        dniPacienteOriginal = null;
        if (citasController != null) {
            citasController.cargarDatosPaciente(null);
        }
    }

    @FXML
    private void eliminarPaciente(){

    Paciente seleccionado = tablaPacientes
                .getSelectionModel()
                .getSelectedItem();

        if(seleccionado == null){
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.setContentText("¿Desea eliminar el paciente " + seleccionado.getDni() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    int deleted = new PacienteDAOMySQL().eliminarPaciente(seleccionado.getDni());
                    String mensaje = deleted == 0
                            ? "No se encontró el paciente para eliminar."
                            : "Paciente eliminado correctamente.";
                    new Alert(Alert.AlertType.INFORMATION, mensaje).showAndWait();
                    cargarPacientes();
                    limpiarFormulario();
                } catch (SQLException e) {
                    mostrarError("No se pudo eliminar el paciente.", e);
                }

            } else {
                return;
            }

    }

    @FXML 
    private void cargarPaciente(){
        Paciente pacienteSelect = tablaPacientes
                    .getSelectionModel()
                    .getSelectedItem();

        if(pacienteSelect == null){
            return;
        }

        Paciente paciente;
        try {
            paciente = new PacienteDAOMySQL().buscarPaciente(pacienteSelect.getDni());
        } catch (SQLException e) {
            mostrarError("No se pudieron cargar los datos del paciente.", e);
            return;
        }

        if(paciente == null){
            return;
        }

        pacienteExiste = true;
        dniPacienteOriginal = paciente.getDni();
        dnipac.setText(paciente.getDni());
        dnipac.setEditable(false);
        apelpac.setText(paciente.getApellidos());
        nompac.setText(paciente.getNombre());
        movilpac.setText(paciente.getMovil());
        emailpac.setText(paciente.getEmail());
        nacpac.setValue(paciente.getNacimiento());
        dirpac.setText(paciente.getDireccion());
        propac.setValue(paciente.getProvincia());
        cargarMunicipios();
        munipac.setValue(paciente.getMunicipio());

        if (citasController != null) {
            citasController.cargarDatosPaciente(paciente);
        }

    }



    @FXML 
    private void buscaPacdni(){
        String dniBuscado = dnipac.getText().trim().toUpperCase();

        Paciente paciente;
        try {
            paciente = new PacienteDAOMySQL().buscaPacdni(dniBuscado);
        } catch (SQLException e) {
            mostrarError("No se pudo buscar el paciente.", e);
            return;
        }
        
        if(paciente == null){
            limpiarFormulario();
            dnipac.setText(dniBuscado);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Información");
            alert.setHeaderText(null);
            alert.setContentText("El paciente no existe.");
            alert.showAndWait();
            return;
        }else{
            pacienteExiste= true;
            dniPacienteOriginal = paciente.getDni();
        }

        dnipac.setText(paciente.getDni());
        dnipac.setEditable(false);
        apelpac.setText(paciente.getApellidos());
        nompac.setText(paciente.getNombre());
        movilpac.setText(paciente.getMovil());
        emailpac.setText(paciente.getEmail());
        nacpac.setValue(paciente.getNacimiento());
        dirpac.setText(paciente.getDireccion());
        propac.setValue(paciente.getProvincia());
        cargarMunicipios();
        munipac.setValue(paciente.getMunicipio());

        if (citasController != null) {
            citasController.cargarDatosPaciente(paciente);
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