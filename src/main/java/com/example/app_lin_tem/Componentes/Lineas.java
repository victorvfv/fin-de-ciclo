package com.example.app_lin_tem.Componentes;

import com.example.app_lin_tem.Componentes.Controller.LineasController;
import com.example.app_lin_tem.Model.Periodo;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class Lineas extends AnchorPane {


    public LineasController ctr;

    public Lineas(Periodo periodo){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/Componentes/Lineas.fxml"));
        ctr= new LineasController(periodo);
        loader.setRoot(this);
        loader.setController(ctr);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ctr.setInfo();
    }


}
