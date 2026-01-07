package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Componentes.hitoLineas;
import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Hito;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;


public class hitoLineasController {
    @FXML
    private Label titulo;

    @FXML
    private Line linea ;

    private Hito hito;

    private MainViewController ctr;

    private hitoLineas nodo;

    public hitoLineas getNodo() {
        return nodo;
    }

    public void setNodo(hitoLineas nodo) {
        this.nodo = nodo;
    }

    public hitoLineasController(Hito hito) {
        this.hito = hito;
    }

    public void setInfo(){
        titulo.setText(hito.getTitulo()+" "+hito.getFecha());
    }

    public void setCtr(MainViewController ctr) {
        this.ctr = ctr;
    }

    public void inicioHito(){
        linea.setStartY(-hito.getAlturaDep());
    }



    @FXML
    public void ratonDentro(){
        linea.setStrokeWidth(3);
        titulo.setOpacity(1);
        titulo.toFront();
        ctr.sendFront(nodo);
    }

    @FXML
    public void ratonFuera(){
        titulo.setOpacity(0.75);
        linea.setStrokeWidth(1);
    }

    @FXML
    public void ratonDentroLinea(){
        linea.setStrokeWidth(3);
        titulo.setOpacity(1);
        titulo.toFront();
        ctr.sendFront(nodo);
    }

    @FXML
    public void ratonFueraLinea(){
        titulo.setOpacity(0.75) ;
        linea.setStrokeWidth(1);
    }
}
