package com.example.app_lin_tem.Model;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class Periodo {

    private String Titulo,Id;
    private int Fecha1;
    private int Fecha2;
    private double Duracion,Altura,AlturaMax;
    private String Datos;
    private Periodo Dependencia;
    private String Color;
    private String imagen;
    private HashSet<String> IdDependientes,IdHitos;
    private transient ArrayList<Periodo> periodosDependientes;
    private transient ArrayList<Hito> hitosDependientes;

    public Periodo() {
        Titulo="";
        Fecha1=0;
        Fecha2=0;
        Datos="";
        Id= UUID.randomUUID().toString();
        periodosDependientes = new ArrayList<>();
        hitosDependientes= new ArrayList<>();
        Duracion=0;
        imagen = null;
        Altura=0;
        AlturaMax=0;
        IdDependientes=new HashSet<>();
        IdHitos=new HashSet<>();
    }

    public Periodo(String titulo, int fecha1, int fecha2, String datos,Color color,String id) {
        Titulo = titulo;
        Fecha1 = fecha1;
        Fecha2 = fecha2;
        Datos = datos;
        Color = color.toString();
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
    }

    public String getId() {
        return Id;
    }


    public ArrayList<Periodo> getPeriodosDependientes() {
        return periodosDependientes;
    }

    public void setPeriodosDependientes(ArrayList<Periodo> periodosDependientes) {
        this.periodosDependientes = periodosDependientes;
    }

    public void addDependientes(Periodo periodo){
        IdDependientes.add(periodo.Id);
        periodosDependientes.add(periodo);
        periodosDependientes.sort((per1, per2)->{
            return (int) -(per1.Duracion-per2.Duracion);
        });
    }

    public void setHitosDependientes(ArrayList<Hito> hitosDependientes) {
        this.hitosDependientes = hitosDependientes;
    }

    public ArrayList<Hito> getHitosDependientes() {
        return hitosDependientes;
    }

    public void addHitosDependientes(Hito hito){
        IdHitos.add(hito.getID());
        hitosDependientes.add(hito);
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
        if(!(periodosDependientes.isEmpty())){
            for(Periodo periodo: periodosDependientes){
                periodo.addAltura(añadido+58);
            }
        }
    }

    public void addAlturaColision(double añadido){
        double alt=getAltura();
        Altura=añadido;
        if(!(periodosDependientes.isEmpty())){
            for(Periodo periodo: periodosDependientes){
                periodo.addAltura((periodo.getAltura()+(añadido-alt)));
            }
        }
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(Image imagen) {
        this.imagen = imagen.getUrl();
    }

    public double getAlturaMax() {
        return AlturaMax;
    }

    public void setAlturaMax() {
        AlturaMax = Altura;
        if(!periodosDependientes.isEmpty()){
            for(Periodo per: periodosDependientes){
                AlturaMax=Math.max(AlturaMax, per.Altura);
                per.setAlturaMax();
            }
        }
    }

}
