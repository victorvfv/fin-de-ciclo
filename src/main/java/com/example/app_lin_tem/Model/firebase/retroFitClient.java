package com.example.app_lin_tem.Model.firebase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public record retroFitClient() {
    public static fireBaseAuth authApi() {
        return new Retrofit.Builder()
                .baseUrl("https://identitytoolkit.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(fireBaseAuth.class);
    }

    public static fireBaseData databaseApi() {
        return new Retrofit.Builder()
                .baseUrl("https://timeaftertime-d2ad9-default-rtdb.europe-west1.firebasedatabase.app/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(fireBaseData.class);
    }

    public static fireBaseTokenApi tokenApi(){
        return new Retrofit.Builder().baseUrl("https://securetoken.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(fireBaseTokenApi.class);
    }
}
