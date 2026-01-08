package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Proyecto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Map;

public class ProyectosNubeController {


    private Map<String, Proyecto> proyectos;
    private MainViewController ctr;
    public boolean cargar;
    public boolean eliminar;

    @FXML
    public VBox VboxBtn;


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

    public Map<String, Proyecto> getProyectos() {
        return proyectos;
    }

    public void setProyectos(Map<String, Proyecto> proyectos) {
        this.proyectos = proyectos;
    }

    public void proyectosBtn()  {
        for(Proyecto proyecto:proyectos.values()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/app_lin_tem/Componentes/ProyectosNubeBtn.fxml"));

            try {
                Parent nodo = loader.load();

                ProyectosNubeBtnController ctr= loader.getController();
                ctr.setEliminar(eliminar);
                ctr.setProyecto(proyecto);
                ctr.setCtr(this.ctr);
                ctr.setCargar(cargar);
                ctr.setParetController(this);
                ctr.setNodo(nodo);
                VboxBtn.getChildren().add(nodo);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void delNodo(Node node){
        VboxBtn.getChildren().remove(node);
    }
}
