package com.example.app_lin_tem.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

public class LoginController {

    @FXML
    private Label newLog,loginText,Link,ErrCarac,ErrMayus,ErrEsp,ErrNum;

    @FXML
    private TextField email,password;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ToggleButton TBVer;

    private String emailS, contrasena;

    private boolean incio;

    @FXML
    private void contrasenaNvisible(){
        contrasena=passwordField.getText();
        password.setText(contrasena);
        comprobarPassword();
    }

    @FXML
    private void contrasenaVisble(){
        contrasena=password.getText();
        passwordField.setText(contrasena);
        comprobarPassword();
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
        }
        else{
            ErrEsp.setVisible(true);
            ErrNum.setVisible(true);
            ErrMayus.setVisible(true);
            ErrCarac.setVisible(true);
            newLog.setVisible(true);
        }
        this.incio=inicio;
    }
}
