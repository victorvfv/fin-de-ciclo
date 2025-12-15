package com.example.app_lin_tem.Componentes.Controller;

import com.example.app_lin_tem.Controllers.MainViewController;
import com.example.app_lin_tem.Model.Hito;
import com.example.app_lin_tem.Model.Periodo;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;

public class HitoController  {

    @FXML
    private TextField NomHit;

    @FXML
    private TextArea Dato;

    @FXML
    private ColorPicker Color;

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
        Dato.setText(Dato.getText()+" "+hito.getFecha());
        Dato.requestFocus();
        Pestaña.setText("Periodo: "+NomHit.getText()+" "+hito.getFecha()+" ");
    }

    @FXML
    protected void OnActionAC(){
        hito.setFecha(hito.getFecha()*(-1));
        Dato.setText(Dato.getText()+" "+hito.getFecha());
    }

    @FXML
    protected void  OnActionTitulo(){
        hito.setTitulo(NomHit.getText());
        Dato.setText(Dato.getText()+" "+hito.getTitulo());
        Fecha.requestFocus();
        Pestaña.setText("Periodo: "+hito.getTitulo()+" "+Fecha.getText()+" ");
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
        for (Periodo periodo1 : periodos) {

            if (periodo1.getTitulo().equals(Dependencias.getValue())) {
                hito.setDependencia(periodo1);
                bloqAC(true);

            } else if (" ".equals(Dependencias.getValue())) {
                hito.setDependencia(null);
                bloqAC(false);
            }
        }
    }


    @FXML
    public void eliminar(){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText("¿Eliminar este Hito?");
        ButtonType btn =confirmation.showAndWait().get();
        if(btn==ButtonType.OK){
            mainViewController.delVistaHit(hito);
        }
    }

    public void setComboBox(String txt){
        Dependencias.setValue(txt);
    }

    public void bloqAC(Boolean estado){
        AC.setDisable(estado);
    }
}
