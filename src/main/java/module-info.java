module com.clase {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.clase to javafx.fxml;
    exports com.clase;
}
