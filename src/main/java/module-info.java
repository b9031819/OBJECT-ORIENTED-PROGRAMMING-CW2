module assignment2.Controllers {
    requires com.google.gson;
    requires json.simple;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.rmi;


    opens assignment2.View to javafx.fxml;
    exports assignment2.View;
}