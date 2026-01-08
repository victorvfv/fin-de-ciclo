package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Proyecto;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class ProyectosNubeBtnController {
    public MainViewController ctr;
    public Proyecto proyecto;
    public boolean cargar;
    public boolean eliminar;
    public Node nodo;
    public ProyectosNubeController paretController;

    @FXML
    public Button btnProyecto,btnDel;

    public ProyectosNubeController getParetController() {
        return paretController;
    }

    public void setParetController(ProyectosNubeController paretController) {
        this.paretController = paretController;
    }

    public Node getNodo() {
        return nodo;
    }

    public void setNodo(Node nodo) {
        this.nodo = nodo;
    }

    public boolean isEliminar() {
        return eliminar;
    }

    public void setEliminar(boolean eliminar) {
        this.eliminar = eliminar;
    }

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
        btnProyecto.setDisable(eliminar);
        btnDel.setDisable(!eliminar);
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

    @FXML
    public void eliminarProyecto(){
        if (ctr.eliminarProyecto(proyecto.getId())) {
            paretController.delNodo(nodo);
        }
        else {
            ctr.toast("Ha habido un fallo al eliminar el proyecto");
        }
    }
}
