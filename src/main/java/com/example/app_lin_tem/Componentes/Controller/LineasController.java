package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Periodo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;

public class LineasController {
    @FXML
    private Label fch1,fch2,titulo;

    @FXML
    private AnchorPane base;

    @FXML
    private Rectangle linea;

    private String id;
    private MainViewController ctr;

    private Double red=0.0,blue=0.0,green=0.0;

    private Periodo periodo;

    public LineasController(Periodo periodo) {
        id= UUID.randomUUID().toString();
        this.periodo=periodo;
    }

    /**
     * Ajusta el tamaño visual de la línea y
     * de su contenedor base según la duración del periodo.
     *
     * @param n ancho a asignar
     */
    public void setTamaño(double n){
        base.setPrefWidth(n);
        linea.setWidth(n);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public void setCtr(MainViewController ctr) {
        this.ctr = ctr;
    }

    /**
     * Evento que se ejecuta cuando el ratón
     * entra en la línea del periodo.
     * Aumenta la opacidad para resaltar el elemento.
     */
    @FXML
    public void Ratondentro(){

        linea.setOpacity(0.75);
    }

    /**
     * Evento que se ejecuta cuando el ratón
     * sale de la línea del periodo.
     * Reduce la opacidad a su estado normal.
     */
    @FXML
    public void Ratonfuera(){
        linea.setOpacity(0.5);
    }

    /**
     * Carga la información del periodo en la vista:
     * - Fechas
     * - Título
     * - Color asociado
     */
    public void setInfo() {
        fch1.setText(""+ Math.abs(periodo.getFecha1()));
        fch2.setText(""+Math.abs(periodo.getFecha2()));
        titulo.setText(periodo.getTitulo());
        linea.setFill(Color.web(periodo.getColor()) );
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    /**
     * Evento asociado al click sobre la línea.
     *
     * Abre una nueva ventana que muestra
     * la información detallada del periodo
     * utilizando un WebView.
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
            ctr.setDataPer(periodo.getTitulo(), periodo.getDatos(), periodo.getFecha1(), periodo.getFecha2(), periodo.getPeriodosDependientes(), periodo.getImagen(),periodo.getHitosDependientes());
            ctr.cargarHtml();
        } catch (IOException _) {}

    }
}
