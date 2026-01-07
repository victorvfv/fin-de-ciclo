package com.example.app_lin_tem.Model;

import java.awt.*;
import java.util.UUID;

public class Hito {


    private String Titulo, Id;
    private int Fecha;
    private String Datos;
    private double Altura, alturaDep;
    private String imagen;
    private String dependenciaId;
    private transient Periodo Dependencia;


    public Hito() {
        Id = UUID.randomUUID().toString();
        Titulo = "";
        Fecha = 0;
        Datos = "";
    }

    public Hito(String titulo, int fecha, String datos, Periodo dependencia,String id) {
        Titulo = titulo;
        Fecha = fecha;
        Datos = datos;
        Dependencia = dependencia;
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


    public double getAltura() {
        return Altura;
    }

    public void setAltura(double altura) {
        Altura=altura;
    }

    public void addAltura(double add){
        Altura=Altura+add;
    }

    public void setAlturaDep(){
        alturaDep=Dependencia.getAltura();
    }

    public void setAlturaNDep(double alturaDep){
        this.alturaDep=alturaDep;
    }

    public Periodo getPadreCaja(Periodo dependencia){
        if(dependencia!=null){
            if(dependencia.getDependencia()==null){
            return dependencia;
        }
        return getPadreCaja(dependencia.getDependencia());
    }
        return null;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen.replace("\\","/");;
    }

    public String getDependenciaId() {
        return dependenciaId;
    }

    public void setDependenciaId(String dependenciaId) {
        this.dependenciaId = dependenciaId;
    }

    public double getAlturaDep() {
        return alturaDep;
    }

    public void setAlturaDep(double alturaDep) {
        this.alturaDep = alturaDep;
    }


}
