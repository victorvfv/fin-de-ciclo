module com.example.app_lin_tem {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires java.desktop;


    opens com.example.app_lin_tem to javafx.fxml;
    exports com.example.app_lin_tem;


    exports com.example.app_lin_tem.Controllers;
    opens com.example.app_lin_tem.Controllers to javafx.fxml;

    opens com.example.app_lin_tem.Componentes to javafx.fxml;
    exports com.example.app_lin_tem.Componentes;

    opens com.example.app_lin_tem.Componentes.Controller to javafx.fxml;
    exports com.example.app_lin_tem.Componentes.Controller;



}