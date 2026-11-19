package com.clase.informes;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.net.URL;

import com.clase.persistencia.ConexionMySQL;

import javafx.scene.control.Alert;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

public class imprimirPacientes {

    public static void mostrar() {
        try (var conexion = ConexionMySQL.getConexion();
                InputStream informe = imprimirPacientes.class.getResourceAsStream(
                        "/informes/pacientes.jrxml")) {
            if (informe == null) {
                throw new IOException("No se encuentra pacientes.jrxml en resources.");
            }

            URL logo = imprimirPacientes.class.getResource("/img/sanidad.png");
            if (logo == null) {
                throw new IOException("No se encuentra sanidad.png en resources/img.");
            }

            JasperReport jasperReport = JasperCompileManager.compileReport(informe);
            HashMap<String, Object> parametros = new HashMap<>();
            parametros.put("LOGO_URL", logo);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport,
                    parametros,
                    conexion);

            JasperViewer.viewReport(jasperPrint, false);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error al generar el informe");
            alert.setHeaderText("No se ha podido generar el informe de pacientes");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
