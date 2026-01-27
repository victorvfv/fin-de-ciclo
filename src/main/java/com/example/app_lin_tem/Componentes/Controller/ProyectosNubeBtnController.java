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

    /**
     * Asigna el proyecto al botón y configura
     * el estado visual de los botones según el modo.
     *
     * - Establece el nombre del proyecto
     * - Activa o desactiva botones según el modo eliminación
     *
     * @param proyecto proyecto asociado
     */
    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
        btnProyecto.setText(proyecto.getNombre());
        btnProyecto.setDisable(eliminar);
        btnDel.setDisable(!eliminar);
    }

    /**
     * Acción asociada al botón principal del proyecto.
     *
     * - Si está en modo carga, carga el proyecto y
     *   rellena la vista principal.
     * - Si no, añade el proyecto al sistema.
     */
    @FXML
    public void cargarProyecto(){
        if(cargar){
        ctr.proyectoCargado(proyecto);
        ctr.rellenarVbox(proyecto.getPeriodos(),proyecto.getHitos());}
        else{
            ctr.anadirProyecto(proyecto);
        }
    }

    /**
     * Acción asociada al botón de eliminación.
     *
     * Elimina el proyecto de la nube y, si la operación
     * es correcta, elimina también su nodo visual.
     * En caso contrario, muestra un mensaje de error.
     */
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
