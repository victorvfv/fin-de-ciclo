package com.example.app_lin_tem.Model.firebase;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public record retroFitClient() {
    public static fireBaseAuth authApi() {
        return new Retrofit.Builder()
                .baseUrl("https://identitytoolkit.googleapis.com/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(fireBaseAuth.class);
    }

    public static fireBaseData databaseApi() {
        return new Retrofit.Builder()
                .baseUrl("https://TU_DATABASE.firebaseio.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(fireBaseData.class);
    }
}
