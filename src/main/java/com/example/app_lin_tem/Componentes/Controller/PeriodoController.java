package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Periodo;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;


import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class PeriodoController {

    @FXML
    private TextField NomPer;

    @FXML
    private TextArea Dato;

    @FXML
    private ColorPicker color;

    @FXML
    private TextField Fecha1;

    @FXML
    private TextField Fecha2;

    @FXML
    private ComboBox Dependencias;

    @FXML
    private TitledPane Pestaña;

    public void setDatos(){
        NomPer.setText(periodo.getTitulo());
        Dato.setText(periodo.getDatos());
        Fecha1.setText(periodo.getFecha1()+"");
        Fecha2.setText(periodo.getFecha2()+"");
        try{
        Dependencias.setValue(periodo.getDependencia().getTitulo());}catch (NullPointerException _){}
        Pestaña.setText("Perido: "+periodo.getTitulo()+" "+periodo.getFecha1()+"-"+periodo.getFecha2());
        color.setValue((Color.web(periodo.getColor())));
    }

    private Periodo periodo;

    private String fechaPrev1,fechaPrev2;

    private MainViewController mainViewController;

    private ArrayList<Periodo> periodos;

    public PeriodoController(){
        periodo = new Periodo();
        periodos = new ArrayList<>();


    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public void setMainViewController(MainViewController mainViewController){
        this.mainViewController = mainViewController;

    }

    public void setPeriodos(ArrayList periodos){
        this.periodos=periodos;

    }

    @FXML
    protected void OnFechaChanged1(){
        if(Fecha1.getText().equals("0")){

            int pos = Fecha1.getCaretPosition();
            Fecha1.setText("1");
            Fecha1.positionCaret(pos);

        }
        if(!Fecha1.getText().matches("-?[0-9]*")) {
            if(Fecha1.getText().contains(" ")){Fecha2.requestFocus();}
            int pos = Fecha1.getCaretPosition()-1;
            Fecha1.setText(fechaPrev1);

            Fecha1.positionCaret(pos);
        }

    }

    @FXML
    protected void OnFechaKeyPress1(){
        if(Fecha1.getText().matches("-?[0-9]*")) {
            fechaPrev1 = Fecha1.getText();


        }
    }

    @FXML
    protected void OnFechaChanged2(){
        if(Fecha2.getText().equals("0")){
            int pos = Fecha2.getCaretPosition();
            Fecha2.setText("1");
            Fecha2.positionCaret(pos);
        }
        if(!Fecha2.getText().matches("[0-9]*")) {
            int pos = Fecha2.getCaretPosition()-1;
            Fecha2.setText(fechaPrev2);
            Fecha2.positionCaret(pos);



        }

    }

    @FXML
    protected void OnFechaKeyPress2(){
        if(Fecha2.getText().matches("[0-9]*")) {

            fechaPrev2 = Fecha2.getText();

        }
    }

    @FXML
    protected void  OnActionFecha2(){
        int fecha1;
        int fecha2;
        try {
            fecha1=Integer.parseInt(Fecha1.getText());
        } catch (NumberFormatException e) {
            fecha1=1;
            Fecha1.setText(""+fecha1);
        }
        try {
            fecha2=Integer.parseInt(Fecha2.getText());
        } catch (NumberFormatException e) {
            fecha2=fecha1+1;
            Fecha2.setText(""+fecha2);
        }

        if(fecha1>fecha2){
            fecha1=fecha1*(-1);
            fecha2=fecha2*(-1);
        }
        periodo.setFecha1(fecha1);
        periodo.setFecha2(fecha2);
        Dato.setText(Dato.getText()+" "+periodo.getFecha1()+" "+periodo.getFecha2());
        Dato.requestFocus();
        Pestaña.setText("Periodo: "+NomPer.getText()+" "+periodo.getFecha1()+" "+periodo.getFecha2());
        mainViewController.setDurFchMinFchMax(periodo.getDuracion(),periodo.getFecha1(),periodo.getFecha2());
    }

    @FXML
    protected void  OnActionTitulo(){
        periodo.setTitulo(NomPer.getText());
        Dato.setText(Dato.getText()+" "+periodo.getTitulo());
        Fecha1.requestFocus();
        Pestaña.setText("Periodo: "+periodo.getTitulo()+" "+Fecha1.getText()+" "+Fecha2.getText());
    }

    @FXML
    protected void setColor(){
        periodo.setColor(color.getValue());
    }

    @FXML
    protected void onActionComboBox(){
        for(Periodo periodoDep: periodos){
                if(periodoDep.getTitulo().equals(Dependencias.getValue())){

                    periodo.setDependencia(periodoDep);
                    if(periodo.getDependencia().getPeriodosDependientes().size()<1){
                        mainViewController.bloqCamposPorDepencdencia(periodo.getDependencia());
                    }
                    if(periodo.getDependencia()!=null){
                        periodo.getDependencia().getPeriodosDependientes().remove(periodo);
                    }
                    periodoDep.addDependientes(periodo);
                    Color color= javafx.scene.paint.Color.web(periodoDep.getColor());
                    newColor(colorDependiente(color.getRed(),color.getGreen(),color.getBlue()));



                } else if (" ".equals(Dependencias.getValue())) {
                    try{
                        periodo.getDependencia().getPeriodosDependientes().remove(periodo);

                        if(periodo.getDependencia().getPeriodosDependientes().isEmpty()){
                            mainViewController.bloqCamposPorDepencdencia(periodo.getDependencia());
                        }
                    }catch (NullPointerException _){}
                    periodo.setDependencia(null);
                }
        }




    }

    @FXML
    public void setDependecias(){
        ArrayList<String> nombres = new ArrayList<>();
        nombres.add(" ");
        for(Periodo dependencia: periodos){
            boolean DepCircular=false;
            Periodo posibleDepCircular;

            posibleDepCircular= dependencia.getDependencia();


            while (posibleDepCircular!=null){

                if (posibleDepCircular.getId().equals(periodo.getId())) {
                    DepCircular = true;
                }
                posibleDepCircular=posibleDepCircular.getDependencia();
            }
            if ((!dependencia.getId().equals(periodo.getId())) &(periodo.getFecha1()>=dependencia.getFecha1()) &periodo.getFecha1()<dependencia.getFecha2()&periodo.getFecha2()<=dependencia.getFecha2() &(!DepCircular)) {
                try {
                    nombres.add(dependencia.getTitulo());
                }catch (NullPointerException _){}
            }
        }
        Dependencias.setItems(FXCollections.observableList(nombres));

    }

    public Periodo getPeriodo() {
        return periodo;
    }

    @FXML
    public void eliminar(){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("¿Eliminar este periodo?");
        ButtonType btn =confirmation.showAndWait().get();
        if(btn==ButtonType.OK){
            if(periodo.getDependencia()!=null){
            periodo.getDependencia().getPeriodosDependientes().remove(periodo);}
            mainViewController.delVistaPer(periodo);
        }
    }

    @FXML
    protected void setDato(){
        periodo.setDatos(Dato.getText());
    }

    public void setComboBox(String txt){
        Dependencias.setValue(txt);
    }

    public void newColor(Color color){
        this.color.setValue(color);
        periodo.setColor(color);
    }

    public void bloquearHabilitarCampos(){
        Fecha1.setDisable(!Fecha1.isDisable());
        Fecha2.setDisable(!Fecha2.isDisable());
        NomPer.setDisable(!NomPer.isDisable());
    }

    private Color colorDependiente(Double red,Double green, Double blue){
        Random random = new Random();
        double mayor =Math.max(red,blue);
        mayor=Math.max(mayor,green);
        if(red==mayor){
            return new Color(red, random.nextDouble(0.75), random.nextDouble(0.75), 1);
        } else if (green==mayor) {
            return new Color(random.nextDouble(0.75), green, random.nextDouble(0.75), 1);
        }else if (blue==mayor) {
            return new Color(random.nextDouble(0.75), random.nextDouble(0.75), blue, 1);
        }
        else{
            return  new Color(random.nextDouble(1),random.nextDouble(1),random.nextDouble(1),1);
        }
    }

    @FXML
    protected void añadirImagen(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("selecciona la imagen");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter(
                        "Archivos de imagen (*.png, *.jpg, *.jpeg)",
                        "*.png", "*.jpg", "*.jpeg"
                );

        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setSelectedExtensionFilter(imageFilter);

        File Url =fileChooser.showOpenDialog(null);
        if(Url!=null){
            periodo.setImagen(Url.getAbsolutePath());
        }
    }


}
