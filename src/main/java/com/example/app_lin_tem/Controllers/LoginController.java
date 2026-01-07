package com.example.app_lin_tem.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class LoginController {

    @FXML
    private Label newLog,loginText,Link,ErrCarac,ErrMayus,ErrEsp,ErrNum;

    @FXML
    private TextField email,password;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ToggleButton TBVer;

    @FXML
    private Button botonAcep;

    private String emailS, contrasena;

    private boolean incio;

    private MainViewController ctr;

    @FXML
    private void contrasenaNvisible(){
        contrasena=passwordField.getText();
        password.setText(contrasena);
        if(!incio){
            comprobarPassword();
        }
    }

    @FXML
    private void contrasenaVisble(){
        contrasena=password.getText();
        passwordField.setText(contrasena);
        if(!incio){
            comprobarPassword();
        }

    }

    @FXML
    private void passwordSpoiler(){
        password.setVisible(!password.isVisible());
        passwordField.setVisible((!passwordField.isVisible()));
    }

    private void comprobarPassword(){
        boolean vbCar=false,vbNum=false,vbEsp=false,vbMay=false;
        if(contrasena.length()>=8){
            vbCar=true;
        }
        if(contrasena.matches(".*[0-9].*")){
            vbNum=true;
        }
        if(contrasena.matches(".*[A-Z].*")){
            vbMay=true;
        }
        if(contrasena.matches(".*[^a-zA-Z0-9].*")){
            vbEsp=true;
        }
        ErrCarac.setVisible(!vbCar);
        ErrNum.setVisible(!vbNum);
        ErrMayus.setVisible(!vbMay);
        ErrEsp.setVisible(!vbEsp);
    }

    public void setVista(boolean inicio){
        if(inicio){
            loginText.setVisible(true);
            Link.setVisible(true);
            botonAcep.setText("Iniciar sesión");
        }
        else{
            ErrEsp.setVisible(true);
            ErrNum.setVisible(true);
            ErrMayus.setVisible(true);
            ErrCarac.setVisible(true);
            newLog.setVisible(true);
            botonAcep.setText("crear sesión");
        }
        this.incio=inicio;
    }

    public void setCtr(MainViewController ctr){
        this.ctr=ctr;
    }


    @FXML
    public void crearOiniciar() throws IOException {
        if(contrasena.length()>=8&&email.getText().length()>0){
            if(incio){
            ctr.iniciarSesionUI(email.getText(),contrasena);
        }
        else{
            ctr.crearSesionUI(email.getText(),contrasena);
        }
    }
    }
}
