package com.example.app_lin_tem.Model;


import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class Periodo {

    private String titulo, id;
    private int fecha1;
    private int fecha2;
    private double duracion, altura, alturaMax;
    private String datos;
    private String dependeciaId;
    private String color;
    private String imagen;
    private HashSet<String> idDependientes, idHitos;
    private transient Periodo dependencia;
    private transient  ArrayList<Periodo> periodosDependientes;
    private transient ArrayList<Hito> hitosDependientes;

    public Periodo() {
        titulo ="";
        fecha1 =1;
        fecha2 =2;
        datos ="";
        id = UUID.randomUUID().toString();
        periodosDependientes = new ArrayList<>();
        hitosDependientes= new ArrayList<>();
        duracion =1;
        imagen = null;
        altura =0;
        alturaMax =0;
        idDependientes =new HashSet<>();
        idHitos =new HashSet<>();
    }

    public Periodo(String titulo, int fecha1, int fecha2, String datos,Color color,String id) {
        this.titulo = titulo;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.datos = datos;
        this.color = color.toString();
        this.id =id;
    }

    public HashSet<String> getIdDependientes() {
        return idDependientes;
    }

    public void setIdDependientes(HashSet<String> idDependientes) {
        this.idDependientes = idDependientes;
    }

    public HashSet<String> getIdHitos() {
        return idHitos;
    }

    public void setIdHitos(HashSet<String> idHitos) {
        this.idHitos = idHitos;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getFecha1() {
        return fecha1;
    }

    public void setFecha1(int fecha1) {
        this.fecha1 = fecha1;
    }

    public int getFecha2() {
        return fecha2;
    }

    public void setFecha2(int fecha2) {
        this.fecha2 = fecha2;
        setDuracion();
    }

    public void setDuracion(){
        duracion =Math.abs(fecha2 - fecha1);
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public String getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color.toString();
    }

    public Periodo getDependencia() {
        return dependencia;
    }

    public void setDependencia(Periodo dependencia) {
        this.dependencia = dependencia;
        if(this.dependencia !=null){
        dependeciaId= dependencia.getId();}
    }

    public String getDependeciaId() {
        return dependeciaId;
    }

    public void setDependeciaId(String dependeciaId) {
        this.dependeciaId = dependeciaId;
    }

    public String getId() {
        return id;
    }


    public ArrayList<Periodo> getPeriodosDependientes() {
        return periodosDependientes;
    }

    public void setPeriodosDependientes(ArrayList<Periodo> periodosDependientes) {
        this.periodosDependientes = periodosDependientes;
    }

    public void addDependientes(Periodo periodo){
        idDependientes.add(periodo.id);
        periodosDependientes.add(periodo);
        periodosDependientes.sort((per1, per2)->{
            return (int) -(per1.duracion -per2.duracion);
        });
    }

    public void setHitosDependientes(ArrayList<Hito> hitosDependientes) {
        this.hitosDependientes = hitosDependientes;
    }

    public ArrayList<Hito> getHitosDependientes() {
        return hitosDependientes;
    }

    public void addHitosDependientes(Hito hito){
        idHitos.add(hito.getId());
        hitosDependientes.add(hito);
        hitosDependientes.sort((per1,per2)->{
            return (int) -(per1.getFecha()-per2.getFecha());
        });
    }

    public double getDuracion() {
        return duracion;
    }


    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura = altura;
    }

    public void addAltura(double añadido){
        altura =añadido;
        if(!(periodosDependientes.isEmpty())){
            for(Periodo periodo: periodosDependientes){
                periodo.addAltura(añadido+58);
            }
        }
    }

    public void addAlturaColision(double añadido){
        double alt=getAltura();
        altura =añadido;
        if(!(periodosDependientes.isEmpty())){
            for(Periodo periodo: periodosDependientes){
                periodo.addAltura((periodo.getAltura()+(añadido-alt)));
            }
        }
        if(!hitosDependientes.isEmpty()){
            for(Hito hito: hitosDependientes){
                hito.setAltura(alturaMax);
            }
        }
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String Url) {
        this.imagen = Url.replace("\\","/");
    }

    public double getAlturaMax() {
        return alturaMax;
    }

    public void setAlturaMax() {
        alturaMax = altura;
        if(!periodosDependientes.isEmpty()){
            for(Periodo per: periodosDependientes){
                alturaMax =Math.max(alturaMax, per.altura);
                per.setAlturaMax();
            }
        }

    }

    public void setAlturaMaxHit(){
        alturaMax = alturaMax +100;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAlturaMaxcaja(double valor){
        alturaMax = Math.max(alturaMax,valor);
    }
}
