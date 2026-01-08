package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Hito;
import com.example.app_lin_tem.Model.Periodo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;

public class HitoController  {

    @FXML
    private TextField NomHit;

    @FXML
    private TextArea Dato;

    @FXML
    private ComboBox Dependencias;

    @FXML
    private TextField Fecha;

    @FXML
    private CheckBox AC;

    @FXML
    private TitledPane Pestaña;

    private String fechaPrev;

    private Hito hito;

    private MainViewController mainViewController;

    private ArrayList<Periodo> periodos;

    public HitoController(){
        hito= new Hito();
        periodos = new ArrayList<Periodo>();
    }

    public void setDatos(){
        NomHit.setText(hito.getTitulo());
        Dato.setText(hito.getDatos());
        Fecha.setText(hito.getFecha()+"");
        try{
            Dependencias.setValue(hito.getDependencia().getTitulo());}catch (NullPointerException _){}
        Pestaña.setText("Perido: "+hito.getTitulo()+" "+hito.getFecha());

    }

    public void setHito(Hito hito) {
        this.hito = hito;
    }

    public void setMainViewController(MainViewController mainViewController){
        this.mainViewController = mainViewController;
        NomHit.requestFocus();

    }

    public void setPeriodos(ArrayList periodos){
        this.periodos=periodos;

    }

    public Hito getHito() {
        return hito;
    }

    @FXML
    protected void OnFechaChanged(){
        if(!Fecha.getText().matches("[0-9]*")) {
            int pos = Fecha.getCaretPosition()-1;
            Fecha.setText(fechaPrev);

            Fecha.positionCaret(pos);
        }

    }

    @FXML
    protected void OnFechaKeyPress(){
        if(Fecha.getText().matches("[0-9]*")) {
            fechaPrev = Fecha.getText();


        }
    }

    @FXML
    protected void  OnActionFecha(){
        if(AC.isSelected()){
            hito.setFecha((Integer.parseInt(Fecha.getText()))*(-1));
        }
        else{
            hito.setFecha(Integer.parseInt(Fecha.getText()));
        }
        if(hito.getFecha()==0){
            hito.setFecha(1);
            Fecha.setText("1");
        }
        Dato.setText(Dato.getText()+" "+hito.getFecha());
        Dato.requestFocus();
        Pestaña.setText("Hito: "+NomHit.getText()+" "+Math.abs(hito.getFecha())+" ");
        mainViewController.setDurFchMinFchMax(1.7976931348623157e+308,hito.getFecha(),hito.getFecha());
    }

    @FXML
    protected void OnActionAC(){
        hito.setFecha(hito.getFecha()*(-1));
        Dato.setText(Dato.getText()+" "+hito.getFecha());
    }

    @FXML
    protected void  OnActionTitulo(){

        Fecha.requestFocus();
        Pestaña.setText("Hito: "+hito.getTitulo()+" "+Fecha.getText()+" ");
    }

    @FXML
    public void setDependecias(){
        ArrayList nombres = new ArrayList<>();
        nombres.add(" ");
        for(Periodo dependencia: periodos){
            if(((hito.getFecha()>=dependencia.getFecha1())&hito.getFecha()<dependencia.getFecha2())){
                nombres.add(dependencia.getTitulo());
            }
        }
        Dependencias.setItems(FXCollections.observableList(nombres));

    }

    @FXML
    protected void onActionComboBox() {
        for (Periodo periodoDep : periodos) {

            if (periodoDep.getTitulo().equals(Dependencias.getValue())) {
                hito.setDependencia(periodoDep);
                if(hito.getDependencia().getHitosDependientes().size()<1){
                    mainViewController.bloqCamposPorDepencdencia(hito.getDependencia());
                }
                if(hito.getDependencia()!=null){
                    hito.getDependencia().getPeriodosDependientes().remove(hito);
                }
                if(hito.getDependencia()!=null){
                    hito.getDependencia().getHitosDependientes().remove(hito);
                }
                periodoDep.addHitosDependientes(hito);

                bloqAC(true);

            } else if (" ".equals(Dependencias.getValue())) {
                try{
                Periodo perPadre= hito.getDependencia();
                perPadre.getHitosDependientes().remove(hito);
                if(perPadre.getPeriodosDependientes().isEmpty()&&perPadre.getHitosDependientes().isEmpty()){
                    mainViewController.bloqCamposPorDepencdencia(perPadre);
                }
                hito.setDependencia(null);
                bloqAC(false);}catch (NullPointerException _){}
                hito.setDependencia(null);
            }
        }
    }

    @FXML
    public void eliminar(){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("¿Eliminar este Hito?");
        ButtonType btn =confirmation.showAndWait().get();
        if(btn==ButtonType.OK){
            if(hito.getDependencia()!=null){
                Periodo perPadre= hito.getDependencia();
                hito.getDependencia().getHitosDependientes().remove(hito);
                if(perPadre.getPeriodosDependientes().isEmpty()&&perPadre.getHitosDependientes().isEmpty()){
                    mainViewController.bloqCamposPorDepencdencia(perPadre);
                }
            }
            mainViewController.delVistaHit(hito);
        }
    }

    @FXML
    protected void setTitulo(){
        hito.setTitulo(NomHit.getText());
    }

    @FXML
    protected void setDato(){
        hito.setDatos(Dato.getText());
        Pestaña.setText("Hito: "+hito.getTitulo()+" "+Fecha.getText()+" ");
    }

    public void setComboBox(String txt){
        Dependencias.setValue(txt);
    }

    public void bloqAC(Boolean estado){
        AC.setDisable(estado);
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
            hito.setImagen(Url.getAbsolutePath());
        }
    }
}
