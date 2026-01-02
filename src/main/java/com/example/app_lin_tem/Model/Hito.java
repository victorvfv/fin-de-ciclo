package com.example.app_lin_tem.Model;

import java.awt.*;
import java.util.UUID;

public class Hito {


    private String Titulo, Id;
    private int Fecha;
    private String Datos;
    private String Color;
    private String dependenciaId;
    private transient Periodo Dependencia;


    public Hito() {
        Id = UUID.randomUUID().toString();
    }

    public Hito(String titulo, int fecha, String datos, Periodo dependencia,Color color,String id) {
        Titulo = titulo;
        Fecha = fecha;
        Datos = datos;
        Dependencia = dependencia;
        Color=color.toString();
        Id =id;
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

    public String getColor() {
        return Color;
    }

    public void setColor(Color color) {
        Color = color.toString();
    }

    public Periodo getDependencia() {
        return Dependencia;
    }

    public void setDependencia(Periodo dependencia) {
        Dependencia = dependencia;
        dependenciaId = dependencia.getId();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }


}
