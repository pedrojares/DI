package com.clase;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class VentanaController implements Initializable {

    @FXML
    private PacientesController pacientesViewController;

    @FXML
    private DoctoresController doctoresViewController;

    @FXML
    private CitasController citasViewController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        pacientesViewController.setCitasController(citasViewController);
        doctoresViewController.setCitasController(citasViewController);
    }
    
    @FXML 
    private void salirApp(){
        Platform.exit();
    }
    
    @FXML 
    private void mostrarAcercade() throws IOException{

        FXMLLoader loader = new FXMLLoader(
        getClass().getResource("/com/clase/acercade.fxml")
        );

        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Acerca de");
        Scene scene = new Scene(root);
        scene.getStylesheets().add(
            getClass().getResource("/com/clase/estilos.css").toExternalForm()
        );
        stage.setScene(scene);
        stage.show();
    }

    @FXML 
    private void abrirDir(){
        try {
            new ProcessBuilder("explorer.exe").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
