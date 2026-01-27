package com.example.app_lin_tem.Model;

import java.util.ArrayList;

public class Proyecto {

    private String id;
    private String nombre;
    private ArrayList<Periodo> periodos;
    private ArrayList<Hito> hitos;

    public Proyecto(String id, String nombre, ArrayList<Periodo> periodos, ArrayList<Hito> hitos) {
        this.id = id;
        this.nombre = nombre;
        this.periodos = periodos;
        this.hitos = hitos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Periodo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(ArrayList<Periodo> periodos) {
        this.periodos = periodos;
    }

    public ArrayList<Hito> getHitos() {
        return hitos;
    }

    public void setHitos(ArrayList<Hito> hitos) {
        this.hitos = hitos;
    }

}
