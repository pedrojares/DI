package com.clase;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override 
    public void start(Stage stage) throws IOException{

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("/com/clase/ventana.fxml")
        );

        Scene scene = new Scene(loader.load(), 1280, 800);
        scene.getStylesheets().add(
            getClass().getResource("/com/clase/estilos.css").toExternalForm()
        );
        stage.setTitle("Sanitarios Teis");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}