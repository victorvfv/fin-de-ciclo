package com.example.app_lin_tem.Model;

import java.util.UUID;

public class Hito {


    private String titulo, id;
    private int fecha;
    private String datos;
    private double altura, alturaDep,alturaFinal;
    private String imagen;
    private String dependenciaId;
    private transient Periodo dependencia;


    public Hito() {
        id = UUID.randomUUID().toString();
        titulo = "";
        fecha = 1;
        datos = "";
    }

    public Hito(String titulo, int fecha, String datos, Periodo dependencia,String id) {
        this.titulo = titulo;
        this.fecha = fecha;
        this.datos = datos;
        this.dependencia = dependencia;
        this.id =id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getFecha() {
        return fecha;
    }

    public void setFecha(int fecha) {
        this.fecha = fecha;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }

    public Periodo getDependencia() {
        return dependencia;
    }

    public void setDependencia(Periodo dependencia) {
        this.dependencia = dependencia;
        if(dependencia!=null){
        dependenciaId = dependencia.getId();}
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public double getAltura() {
        return altura;
    }

    public void setAltura(double altura) {
        this.altura =altura;
    }

    public void addAltura(double add){
        altura = altura +add;
    }


    public void setAlturaDep(double alturaDep){
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

    public double getAlturaFinal() {
        return alturaFinal;
    }

    public void setAlturaFinal(double alturaFinal) {
        this.alturaFinal = alturaFinal;
    }
}
