package com.example.app_lin_tem.Model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;

public class JsonMaker {

    Gson gson;

    public JsonMaker() {
        this.gson = new Gson();
    }

    public String getJson(Proyecto proyecto){
        return gson.toJson(proyecto);
    }

    public Proyecto getProyecto(String file){


        return gson.fromJson(file,Proyecto.class);

    }
}
