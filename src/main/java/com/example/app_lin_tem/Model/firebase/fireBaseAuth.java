package com.example.app_lin_tem.Model.firebase;


import retrofit2.Call;
import retrofit2.http.*;

public interface fireBaseAuth {
    //crearCuenta
    @POST("v1/accounts:signUp")
    Call<AuthResponse> crearCuenta(
            @Query("key")String apiKey,
            @Body AuthRequest body
    );

    //inicar sesion
    @POST("v1/accounts:signInWithPassword")
    Call<AuthResponse> iniciarSesion(
            @Query("key")String apiKey,
            @Body AuthRequest body
    );

    @POST("v1/accounts:sendOobCode")
    Call<Void> enviarEmailVerificacion(
            @Query("key") String apiKey,
            @Body OobCodeRequest body
    );

    @POST("v1/accounts:sendOobCode")
    Call<Void> recuperarContrasena(
            @Query("key") String apiKey,
            @Body OobCodeRequest body
    );


}
