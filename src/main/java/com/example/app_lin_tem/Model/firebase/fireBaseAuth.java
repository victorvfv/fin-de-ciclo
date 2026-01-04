package com.example.app_lin_tem.Model.firebase;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface fireBaseAuth {
    //crearCuenta
    @POST("account:singUp")
    Call<AuthResponse> crearCuenta(
            @Query("key")String apiKey,
            @Body AuthRequest body
    );

    //inicar sesion
    @POST("account:singInPassword")
    Call<AuthResponse> iniciarSesion(
            @Query("key")String apiKey,
            @Body AuthRequest body
    );

    @POST("accounts:sendOobCode")
    Call<Void> enviarEmailVerificacion(
            @Query("key") String apiKey,
            @Body OobCodeRequest body
    );

    @POST("accounts:sendOobCode")
    Call<Void> recuperarContrasena(
            @Query("key") String apiKey,
            @Body OobCodeRequest body
    );
}
