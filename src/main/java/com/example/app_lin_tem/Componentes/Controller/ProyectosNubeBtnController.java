package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Proyecto;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ProyectosNubeBtnController {
    public MainViewController ctr;
    public Proyecto proyecto;
    public boolean cargar;

    @FXML
    public Button btnProyecto;

    public boolean isCargar() {
        return cargar;
    }

    public void setCargar(boolean cargar) {
        this.cargar = cargar;
    }

    public MainViewController getCtr() {
        return ctr;
    }

    public void setCtr(MainViewController ctr) {
        this.ctr = ctr;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        btnProyecto.setText(proyecto.getNombre());
    }

    @FXML
    public void cargarProyecto(){
        if(cargar){
        ctr.proyectoCargado(proyecto);
        ctr.rellenarVbox(proyecto.getPeriodos(),proyecto.getHitos());}
        else{
            ctr.anadirProyecto(proyecto);
        }

    }
}
