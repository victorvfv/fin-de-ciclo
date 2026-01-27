package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Componentes.HitoLineas;
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


public class HitoLineasController {
    @FXML
    private Label titulo;

    @FXML
    private Line linea ;

    private Hito hito;

    private MainViewController ctr;

    private HitoLineas nodo;

    public HitoLineas getNodo() {
        return nodo;
    }

    public void setNodo(HitoLineas nodo) {
        this.nodo = nodo;
    }

    public HitoLineasController(Hito hito) {
        this.hito = hito;
    }

    /**
     * Configura la información visual del hito:
     * - Título
     * - Fecha (normal o A.C si es negativa)
     */
    public void setInfo(){
        String fecha = ""+hito.getFecha();
        if(hito.getFecha()<0){
            fecha=Math.abs(hito.getFecha())+" A.C";
        }
        titulo.setText(hito.getTitulo()+" "+fecha);
    }

    public void setCtr(MainViewController ctr) {
        this.ctr = ctr;
    }

    public void inicioHito(){
        linea.setEndY(hito.getAlturaDep());
    }

    /**
     * Evento asociado al click sobre el hito.
     *
     * Abre una ventana con la información detallada
     * del hito utilizando un WebView.
     */
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

    /**
     * Evento cuando el ratón entra sobre el hito
     * (etiqueta).
     *
     * Resalta visualmente el hito y lo trae
     * al primer plano.
     */
    @FXML
    public void ratonDentro(){
        linea.setStrokeWidth(3);
        titulo.setOpacity(1);

        ctr.sendFrontOrBack(nodo,true);
    }

    /**
     * Evento cuando el ratón sale del hito
     * (etiqueta).
     *
     * Devuelve el hito a su estado visual normal.
     */
    @FXML
    public void ratonFuera(){
        titulo.setOpacity(0.75);
        linea.setStrokeWidth(1);
        ctr.sendFrontOrBack(nodo,false);
    }

    /**
     * Evento cuando el ratón entra sobre la línea del hito.
     */
    @FXML
    public void ratonDentroLinea(){
        linea.setStrokeWidth(3);
        titulo.setOpacity(1);

        ctr.sendFrontOrBack(nodo,true);
    }

    /**
     * Evento cuando el ratón sale de la línea del hito.
     */
    @FXML
    public void ratonFueraLinea(){
        titulo.setOpacity(0.75) ;
        linea.setStrokeWidth(1);
        ctr.sendFrontOrBack(nodo,false);
    }


}
