package com.example.app_lin_tem.Componentes;

import com.example.app_lin_tem.Componentes.Controller.HitoLineasController;
import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Hito;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class HitoLineas extends AnchorPane {

    public HitoLineasController ctr;

    public HitoLineas(Hito hito){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/Componentes/hitoLineas.fxml"));
        ctr= new HitoLineasController(hito);
        loader.setRoot(this);
        loader.setController(ctr);
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ctr.setInfo();
    }

    public void inicioHito(MainViewController mainViewController){
        ctr.inicioHito();
        ctr.setNodo(this);
        ctr.setCtr(mainViewController);
    }
}
