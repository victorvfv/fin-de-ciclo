package com.example.app_lin_tem.Model;

import com.google.gson.Gson;

public class JsonMaker {

    Gson gson;

    public JsonMaker() {
        this.gson = new Gson();
    }

    public String getJson(Proyecto proyecto){
        return gson.toJson(proyecto);
    }
}
