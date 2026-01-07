module com.example.app_lin_tem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires com.google.gson;
    requires retrofit2;
    requires retrofit2.converter.gson;
    requires okhttp3;
    requires openhtmltopdf.pdfbox;
    requires org.jsoup;


    opens com.example.app_lin_tem to javafx.fxml;
    exports com.example.app_lin_tem;


    exports com.example.app_lin_tem.Controllers;
    opens com.example.app_lin_tem.Controllers to javafx.fxml;

    opens com.example.app_lin_tem.Componentes to javafx.fxml;
    exports com.example.app_lin_tem.Componentes;

    opens com.example.app_lin_tem.Componentes.Controller to javafx.fxml;
    exports com.example.app_lin_tem.Componentes.Controller;

    opens com.example.app_lin_tem.Model to com.google.gson;

    exports com.example.app_lin_tem.Model.firebase;
    opens com.example.app_lin_tem.Model.firebase to javafx.fxml;

}