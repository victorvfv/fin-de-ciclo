package com.example.app_lin_tem.Componentes;

import com.example.app_lin_tem.Componentes.Controller.HitoController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HitoUI extends TitledPane {

    @FXML
    private TextField NomHit;

    public HitoController ctr;

    public HitoUI() {
        ctr= new HitoController();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/componentes/Hito.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(ctr);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    protected void onNombreCambiado(){
        NomHit.setText("cambiooo");
    }


}