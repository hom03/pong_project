module com.example.pong_project {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires junit;
    requires java.sql;

    opens com.example.pong_project.Controller to javafx.fxml;
    exports com.example.pong_project.Controller;
    exports com.example.pong_project.Model;
    exports com.example.pong_project.View;
    exports com.example.pong_project;
}