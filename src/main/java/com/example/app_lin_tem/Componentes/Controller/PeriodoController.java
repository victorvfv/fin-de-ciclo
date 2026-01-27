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


    private Periodo periodo;

    private String fechaPrev1,fechaPrev2;

    private MainViewController mainViewController;

    private ArrayList<Periodo> periodos;

    public PeriodoController(){
        periodo = new Periodo();
        periodos = new ArrayList<>();


    }

    /**
     * Rellena los campos visuales con los datos del periodo.
     */
    public void setCampos(){
        NomPer.setText(periodo.getTitulo());
        Dato.setText(periodo.getDatos());
        Fecha1.setText(periodo.getFecha1()+"");
        Fecha2.setText(periodo.getFecha2()+"");
        try{
            Dependencias.setValue(periodo.getDependencia().getTitulo());}catch (NullPointerException _){}
        Pestaña.setText("Perido: "+periodo.getTitulo()+" "+periodo.getFecha1()+"-"+periodo.getFecha2());
        color.setValue((Color.web(periodo.getColor())));
    }

    /**
     * Asigna el periodo al controlador.
     * @param periodo periodo a gestionar
     */
    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    /**
     * Asigna el controlador principal.
     * @param mainViewController controlador principal
     */
    public void setMainViewController(MainViewController mainViewController){
        this.mainViewController = mainViewController;

    }

    /**
     * Asigna la lista de periodos disponibles.
     * @param periodos lista de periodos
     */
    public void setPeriodos(ArrayList periodos){
        this.periodos=periodos;

    }

    /**
     * Inicializa las fechas por defecto.
     */
    public void setFechasIni(){
        Fecha1.setText("1");
        Fecha2.setText("2");
    }

    /**
     * Valida cambios en la fecha inicial.
     * Evita ceros y caracteres no numéricos.
     */
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

    /**
     * Guarda el valor previo de la fecha inicial.
     */
    @FXML
    protected void OnFechaKeyPress1(){
        if(Fecha1.getText().matches("-?[0-9]*")) {
            fechaPrev1 = Fecha1.getText();


        }
    }

    /**
     * Valida cambios en la fecha final.
     */
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

    /**
     * Guarda el valor previo de la fecha final.
     */
    @FXML
    protected void OnFechaKeyPress2(){
        if(Fecha2.getText().matches("[0-9]*")) {

            fechaPrev2 = Fecha2.getText();

        }
    }

    /**
     * Aplica las fechas cuando se confirma la edición.
     */
    @FXML
    protected void  OnActionFecha2(){
        setFechas();
    }

    /**
     * Valida y asigna las fechas al periodo.
     * Comprueba:
     * - Orden correcto
     * - Contención en el periodo padre
     * - Actualiza duración global
     */
    private void setFechas(){
        int fchPrev1=periodo.getFecha1();
        int fchPrev2=periodo.getFecha2();
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

        if(fecha1==fecha2){
            fecha2++;
            Fecha2.setText(""+fecha2);
        }

        if(fecha1>fecha2){
            fecha1=fecha1*(-1);
            fecha2=fecha2*(-1);
        }
        if(periodo.getDependencia()!=null){
            boolean fallo=false;
            if(fecha1<periodo.getDependencia().getFecha1()){
                fecha1=fchPrev1;
                Fecha1.setText(""+fecha1);
                fallo=true;
            }
            if(fecha2>periodo.getDependencia().getFecha2()){
                fecha2=fchPrev2;
                Fecha2.setText(""+fecha2);
                fallo=true;
            }
            if(fallo){
                mainViewController.toast("El periodo ha de ser contenido por padre");
            }
        }
        periodo.setFecha1(fecha1);
        periodo.setFecha2(fecha2);
        Dato.requestFocus();
        Pestaña.setText("Periodo: "+NomPer.getText()+" "+Math.abs(periodo.getFecha1())+"-"+Math.abs(periodo.getFecha2()));
        mainViewController.setDurFchMinFchMax(periodo.getDuracion(),periodo.getFecha1(),periodo.getFecha2());
    }

    /**
     * Actualiza la pestaña al cambiar el título
     * */
    @FXML
    protected void  OnActionTitulo(){
        Fecha1.requestFocus();
        Pestaña.setText("Periodo: "+periodo.getTitulo()+" "+Fecha1.getText()+"-"+Fecha2.getText());
    }

    @FXML
    protected void setColor(){
        periodo.setColor(color.getValue());
    }

    /**
     * Actualiza la dependencia del periodo según ComboBox
     * - Ajusta dependencias circulares
     * - Ajusta colores de periodos dependientes
     */
    @FXML
    protected void onActionComboBox(){
        for(Periodo periodoDep: periodos){
                if(periodoDep.getTitulo().equals(Dependencias.getValue())&&(periodoDep.getId()!=periodo.getId())){

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

    /**
     * Rellena el ComboBox con periodos válidos
     * para dependencia evitando ciclos.
     */
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

    /**
     * Elimina el periodo con confirmación.
     * - Actualiza dependencias de otros periodos
     * - Notifica al controlador principal
     */
    @FXML
    public void eliminar(){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("¿Eliminar este periodo?");
        ButtonType btn =confirmation.showAndWait().get();
        if(btn==ButtonType.OK){
            if(periodo.getDependencia()!=null){
                Periodo perPadre= periodo.getDependencia();
                periodo.getDependencia().getPeriodosDependientes().remove(periodo);
                if(perPadre.getPeriodosDependientes().isEmpty()&&perPadre.getHitosDependientes().isEmpty()){
                    mainViewController.bloqCamposPorDepencdencia(perPadre);
                }

            }
                mainViewController.delVistaPer(periodo);
        }
    }

    @FXML
    protected void setTitulo(){
        periodo.setTitulo(NomPer.getText());
        Pestaña.setText("Periodo: "+periodo.getTitulo()+" "+Fecha1.getText()+" "+Fecha2.getText());
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

    /** Bloquea o habilita los campos de entrada */
    public void bloquearHabilitarCampos(){
        Fecha1.setDisable(!Fecha1.isDisable());
        Fecha2.setDisable(!Fecha2.isDisable());
        NomPer.setDisable(!NomPer.isDisable());
    }

    /**
     * Calcula un color derivado para periodos dependientes
     * evitando que el color principal se repita exactamente
     */
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

    /**
     * Abre un FileChooser para seleccionar una imagen
     * asociada al periodo.
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
            periodo.setImagen(Url.getAbsolutePath());
        }
    }


}
