package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Componentes.hitoLineas;
import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Hito;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;


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
        titulo.setText(hito.getTitulo()+" "+Math.abs(hito.getFecha()));
    }

    public void setCtr(MainViewController ctr) {
        this.ctr = ctr;
    }

    public void inicioHito(){
        linea.setEndY(hito.getAlturaDep());
    }

    @FXML
    public void onCLick(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/Componentes/VistaDato.fxml"));

        try {
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();

            VistaDatoController ctr=loader.getController();
            ctr.setDataHit(hito.getTitulo(), hito.getDatos(), hito.getFecha(), hito.getImagen());
            ctr.cargarHtml();
        } catch (IOException _) {}

    }


    @FXML
    public void ratonDentro(){
        linea.setStrokeWidth(3);
        titulo.setOpacity(1);

        ctr.sendFrontOrBack(nodo,true);
    }

    @FXML
    public void ratonFuera(){
        titulo.setOpacity(0.75);
        linea.setStrokeWidth(1);
        ctr.sendFrontOrBack(nodo,false);
    }

    @FXML
    public void ratonDentroLinea(){
        linea.setStrokeWidth(3);
        titulo.setOpacity(1);

        ctr.sendFrontOrBack(nodo,true);
    }

    @FXML
    public void ratonFueraLinea(){
        titulo.setOpacity(0.75) ;
        linea.setStrokeWidth(1);
        ctr.sendFrontOrBack(nodo,false);
    }


}
