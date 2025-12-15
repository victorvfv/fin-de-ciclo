package com.example.app_lin_tem.Model;

import java.awt.*;
import java.util.UUID;

public class Hito {


    private String Titulo,ID;
    private int Fecha;
    private String Datos;
    private Color Color;
    private Periodo Dependencia;


    public Hito() {
        ID= UUID.randomUUID().toString();
    }

    public Hito(String titulo, int fecha, String datos, Periodo dependencia,Color color,String id) {
        Titulo = titulo;
        Fecha = fecha;
        Datos = datos;
        Dependencia = dependencia;
        Color=color;
        ID=id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public int getFecha() {
        return Fecha;
    }

    public void setFecha(int fecha) {
        Fecha = fecha;
    }

    public String getDatos() {
        return Datos;
    }

    public void setDatos(String datos) {
        Datos = datos;
    }

    public Color getColor() {
        return Color;
    }

    public void setColor(Color color) {
        Color = color;
    }

    public Periodo getDependencia() {
        return Dependencia;
    }

    public void setDependencia(Periodo dependencia) {
        Dependencia = dependencia;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


}
