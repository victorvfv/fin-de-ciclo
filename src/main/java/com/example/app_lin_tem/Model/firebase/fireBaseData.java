package com.example.app_lin_tem.Model.firebase;

import com.example.app_lin_tem.Model.Proyecto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface fireBaseData {
    //guardar
    @PUT("proyectos/{uid}/{id}.json")
    Call<Proyecto> saveProyecto(
                @Path("uid") String uid,
                @Path("id") String id,
                @Query("auth") String idToken,
                @Body Proyecto proyecto
    );

    //leer todos los proyectos
    @GET("proyectos/{uid}/.json")
    Call<Map<String,Proyecto>> getProyectos(
            @Path("uid") String uid,
            @Query("auth") String idToken
    );




    // Eliminar proyecto
    @DELETE("proyectos/{uid}/{id}.json")
    Call<Void> deleteProyecto(
            @Path("uid") String uid,
            @Path("id") String id,
            @Query("auth") String idToken
    );
}
