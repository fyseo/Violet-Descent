module com.example.crawler {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;
    requires javafx.media;
    requires java.sql;
    requires jdk.compiler;


    opens com.example.crawler to javafx.fxml;
    exports com.example.crawler;
}