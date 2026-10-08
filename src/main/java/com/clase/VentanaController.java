package com.clase;

import java.io.IOException;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VentanaController {
    
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
        stage.setScene(new Scene(root));
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

