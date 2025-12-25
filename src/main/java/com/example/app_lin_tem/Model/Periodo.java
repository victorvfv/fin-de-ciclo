package com.example.app_lin_tem.Model;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.UUID;

public class Periodo {

    private String Titulo,Id;
    private int Fecha1;
    private int Fecha2;
    private double Duracion,Altura,AlturaMax;
    private String Datos;
    private Periodo Dependencia;
    private Color Color;
    private ArrayList<Periodo> Dependientes;
    private ArrayList<Hito> hitosDependientes;
    private Image imagen;

    public Periodo() {
        Titulo="";
        Fecha1=0;
        Fecha2=0;
        Datos="";
        Id= UUID.randomUUID().toString();
        Dependientes = new ArrayList<>();
        hitosDependientes= new ArrayList<>();
        Duracion=0;
        imagen = null;
        Altura=0;
        AlturaMax=0;
    }

    public Periodo(String titulo, int fecha1, int fecha2, String datos,Color color,String id) {
        Titulo = titulo;
        Fecha1 = fecha1;
        Fecha2 = fecha2;
        Datos = datos;
        Color = color;
        Id=id;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public int getFecha1() {
        return Fecha1;
    }

    public void setFecha1(int fecha1) {
        Fecha1 = fecha1;
    }

    public int getFecha2() {
        return Fecha2;
    }

    public void setFecha2(int fecha2) {
        Fecha2 = fecha2;
        setDuracion();
    }

    public void setDuracion(){
        Duracion =Math.abs(Fecha2 - Fecha1);
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

    public String getId() {
        return Id;
    }


    public ArrayList<Periodo> getDependientes() {
        return Dependientes;
    }

    public void setDependientes(ArrayList<Periodo> dependientes) {
        Dependientes = dependientes;
    }

    public void addDependientes(Periodo periodo){
        Dependientes.add(periodo);
        Dependientes.sort((per1,per2)->{
            return (int) -(per1.Duracion-per2.Duracion);
        });
    }

    public void setHitosDependientes(ArrayList<Hito> hitosDependientes) {
        this.hitosDependientes = hitosDependientes;
    }

    public ArrayList<Hito> getHitosDependientes() {
        return hitosDependientes;
    }

    public void addHitosDependientes(Hito Hito){
        hitosDependientes.add(Hito);
        hitosDependientes.sort((per1,per2)->{
            return (int) -(per1.getFecha()-per2.getFecha());
        });
    }

    public double getDuracion() {
        return Duracion;
    }


    public double getAltura() {
        return Altura;
    }

    public void setAltura(double altura) {
        Altura = altura;
    }

    public void addAltura(double añadido){
        Altura=añadido;
        if(!(Dependientes.isEmpty())){
            for(Periodo periodo:Dependientes){
                periodo.addAltura(añadido+58);
            }
        }
    }

    public void addAlturaColision(double añadido){
        double alt=getAltura();
        Altura=añadido;
        if(!(Dependientes.isEmpty())){
            for(Periodo periodo:Dependientes){
                periodo.addAltura((periodo.getAltura()+(añadido-alt)));
            }
        }
    }

    public Image getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen;
    }

    public double getAlturaMax() {
        return AlturaMax;
    }

    public void setAlturaMax() {
        AlturaMax = Altura;
        if(!Dependientes.isEmpty()){
            for(Periodo per:Dependientes){
                AlturaMax=Math.max(AlturaMax, per.Altura);
                per.setAlturaMax();
            }
        }
    }

}
