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


    public void setTamaño(double n){
        base.setPrefWidth(n);
        linea.setWidth(n);
        //Random random = new Random();
       // double mayor =Math.max(red,blue);
        //mayor=Math.max(mayor,green);
        //Paint pintura = new Color(random.nextDouble(0.75), random.nextDouble(0.75), random.nextDouble(0.75), 1);
        //if(red==mayor){
       //     pintura = new Color(red, random.nextDouble(0.75), random.nextDouble(0.75), 1);
        //} else if (green==mayor) {
        //    pintura = new Color(random.nextDouble(0.75), green, random.nextDouble(0.75), 1);
        //}else if (blue==mayor) {
        //    pintura = new Color(random.nextDouble(0.75), random.nextDouble(0.75), blue, 1);
        //}

        //linea.setFill(pintura);
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

    @FXML
    public void Ratondentro(){

        linea.setOpacity(0.75);
    }

    @FXML
    public void Ratonfuera(){
        linea.setOpacity(0.5);
    }


    public void setInfo() {
        fch1.setText(""+ Math.abs(periodo.getFecha1()));
        fch2.setText(""+Math.abs(periodo.getFecha2()));
        titulo.setText(periodo.getTitulo());
        linea.setFill(Color.web(periodo.getColor()) );
    }

    public Periodo getPeriodo() {
        return periodo;
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
            ctr.setDataPer(periodo.getTitulo(), periodo.getDatos(), periodo.getFecha1(), periodo.getFecha2(), periodo.getPeriodosDependientes(), periodo.getImagen(),periodo.getHitosDependientes());
            ctr.cargarHtml();
        } catch (IOException _) {}

    }
}
