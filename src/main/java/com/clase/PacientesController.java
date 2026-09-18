package com.clase;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;


public class PacientesController implements Initializable{

    @FXML 
    private TextField dnipac, apelpac, nompac, tlfopac, emailpac, dirpac;
    @FXML 
    private ComboBox<String> cmbpac, locpac;
    @FXML  
    private Button btnguardarpac, btnmodifpac;
    @FXML 
    private DatePicker nacpac;
    
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

        //Comprobar mobil
        tlfopac.focusedProperty().addListener((observable, oldValue, newValue) -> {
        if (!newValue) {
            comprobarMovil();
        }
    });

        //Datos de prueba
        cmbpac.getItems().addAll(
            "A Coruña",
            "Lugo",
            "Ourense",
            "Pontevedra"
        );
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
        String movil =tlfopac.getText().trim();
        if(movil.isEmpty()){
            return;
        }
        if(validarMovil(movil)){
            tlfopac.setStyle("");
            tlfopac.setText(movil);
        }else{
            tlfopac.setStyle("-fx-border-color: red;");
            tlfopac.setText("");
        }
    }


    private boolean validarMovil(String telefono) {
    return telefono.matches("[67][0-9]{8}");
    }





    @FXML 
    private void guardarPaiente(){

        String dni=dnipac.getText();        
        String apellidos=apelpac.getText();
        String nombre=nompac.getText();

        LocalDate fechaNacimiento=nacpac.getValue();

        String telefono=tlfopac.getText();
        String email=emailpac.getText();
        String direccion=dirpac.getText();

        String provincia=cmbpac.getValue();
        String localidad=locpac.getValue();

        System.out.println("----------------PACIENTE-------------");
        System.out.println("DNI: "+dni);
        System.out.println("Apellidos: "+apellidos);
        System.out.println("Nombre: "+nombre);
        System.out.println("Fecha Nacimiento: "+fechaNacimiento);
        System.out.println("Telefono: "+telefono);
        System.out.println("Email: "+email);
        System.out.println("Direccion: "+direccion);
        System.out.println("Provincia: "+provincia);
        System.out.println("Localidad: "+localidad);
        System.out.println("--------------------------------------");



    }


    


}
