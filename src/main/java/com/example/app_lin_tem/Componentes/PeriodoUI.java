package com.example.app_lin_tem.Componentes;

import com.example.app_lin_tem.Componentes.Controller.PeriodoController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;



public class PeriodoUI extends TitledPane {

    public PeriodoController ctr;

    public PeriodoUI() {
        ctr= new PeriodoController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/componentes/Periodo.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(ctr);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public PeriodoController getCtr() {
        return ctr;
    }
}