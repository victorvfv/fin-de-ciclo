package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Model.Periodo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;


import java.util.ArrayList;

public class VistaPeriodoController {

    @FXML
    private Label Titulo,Data;

    @FXML
    private ImageView Imagen;

    @FXML
    private TextFlow Periodos,Hitos;


    public void setData(String titulo,String data, int fecha1, int fecha2, ArrayList<Periodo> dependecias, Image imagen){
        Titulo.setText(titulo.trim()+" "+fecha1+"-"+fecha2);
        Data.setText(data.trim());
        //Imagen.setImage(imagen);
        for(Periodo per:dependecias){
            Text text= new Text("* "+per.getTitulo().trim()+"\n");
            Periodos.getChildren().add(text);
        }
    }
}
