package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Hito;
import com.example.app_lin_tem.Model.Periodo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    /**
     * Carga los datos del hito en los campos de la interfaz.
     */
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

    /**
     * Valida que solo se introduzcan números en la fecha.
     */
    @FXML
    protected void OnFechaChanged(){
        if(!Fecha.getText().matches("[0-9]*")) {
            int pos = Fecha.getCaretPosition()-1;
            Fecha.setText(fechaPrev);

            Fecha.positionCaret(pos);
        }

    }

    /**
     * Guarda el valor previo de la fecha si es válida.
     */
    @FXML
    protected void OnFechaKeyPress(){
        if(Fecha.getText().matches("[0-9]*")) {
            fechaPrev = Fecha.getText();


        }
    }

    /**
     * Aplica la fecha al hito cuando se confirma el campo.
     * Valida:
     * - Formato
     * - Valor distinto de 0
     * - Que esté dentro del periodo padre
     */

    @FXML
    protected void  OnActionFecha(){
        int fch= hito.getFecha();
        try{
        if(AC.isSelected()){
            hito.setFecha((Integer.parseInt(Fecha.getText()))*(-1));
        }
        else{
            hito.setFecha(Integer.parseInt(Fecha.getText()));
        }} catch (NumberFormatException e) {
            hito.setFecha(1);
            Fecha.setText("1");
        }
        if(hito.getFecha()==0){
            hito.setFecha(1);
            Fecha.setText("1");
        }
        if(hito.getDependencia()!=null){
            boolean fallo=false;
            if(hito.getFecha()<hito.getDependencia().getFecha1()||hito.getFecha()>hito.getDependencia().getFecha2()){
                hito.setFecha(fch);
                Fecha.setText(""+fch);
                fallo=true;
            }

            if(fallo){
                mainViewController.toast("El hito ha de ser contenido por padre");
            }
        }
        Dato.requestFocus();
        String fecha = ""+hito.getFecha();
        if(hito.getFecha()<0){
            fecha=Math.abs(hito.getFecha())+" A.C";
        }
        Pestaña.setText("Hito: "+NomHit.getText()+" "+fecha+" ");
        mainViewController.setDurFchMinFchMax(1,hito.getFecha(),hito.getFecha());
    }

    /**
     * Cambia el signo de la fecha al marcar A.C.
     */
    @FXML
    protected void OnActionAC(){
        hito.setFecha(hito.getFecha()*(-1));
        Dato.setText(Dato.getText()+" "+hito.getFecha());
    }

    /**
     * Actualiza el título del hito.
     */
    @FXML
    protected void  OnActionTitulo(){

        Fecha.requestFocus();
        Pestaña.setText("Hito: "+hito.getTitulo()+" "+Fecha.getText()+" ");
    }

    /**
     * Carga las dependencias posibles según la fecha del hito.
     */
    @FXML
    public void setDependecias(){
        ArrayList nombres = new ArrayList<>();
        nombres.add(" ");
        for(Periodo dependencia: periodos){
            if(((hito.getFecha()>=dependencia.getFecha1())&hito.getFecha()<=dependencia.getFecha2())){
                nombres.add(dependencia.getTitulo());
            }
        }
        Dependencias.setItems(FXCollections.observableList(nombres));

    }

    /**
     * Asigna o elimina la dependencia seleccionada.
     */
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

    /**
     * Elimina el hito tras confirmación del usuario.
     */
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

    /**
     * Actualiza el título del hito.
     */
    @FXML
    protected void setTitulo(){
        hito.setTitulo(NomHit.getText());
    }

    /**
     * Actualiza los datos del hito.
     */
    @FXML
    protected void setDato(){
        hito.setDatos(Dato.getText());
    }

    public void setComboBox(String txt){
        Dependencias.setValue(txt);
    }

    /** Bloquea o desbloquea el checkbox A.C */
    public void bloqAC(Boolean estado){
        AC.setDisable(estado);
    }

    /**
     * Permite seleccionar una imagen para el hito.
     */
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

    /** Fija la fecha inicial a 1 */
    public void setFechaIni() {
        Fecha.setText("1");
    }
}
