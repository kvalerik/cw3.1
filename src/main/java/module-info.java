module com.example.cwkarapaeva {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cwkarapaeva to javafx.fxml;
    exports com.example.cwkarapaeva;
}