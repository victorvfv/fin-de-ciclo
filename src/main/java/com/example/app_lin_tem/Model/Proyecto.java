package com.example.app_lin_tem.Model;

import java.util.ArrayList;

public class Proyecto {

    private String Id;
    private String Nombre;
    private ArrayList<Periodo> periodos;
    private ArrayList<Hito> hitos;
    private boolean saveFire;
    private transient String url;

    public Proyecto(String id, String nombre, ArrayList<Periodo> periodos, ArrayList<Hito> hitos) {
        Id = id;
        Nombre = nombre;
        this.periodos = periodos;
        this.hitos = hitos;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
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
