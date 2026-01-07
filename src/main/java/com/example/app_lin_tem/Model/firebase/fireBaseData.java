package com.example.app_lin_tem.Model.firebase;

import com.example.app_lin_tem.Model.Proyecto;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface fireBaseData {
    //guardar
    @POST("proyectos/{id}.json")
    Call<Proyecto> saveProyecto(
                @Path("id") String id,
                @Query("auth") String idToken,
                @Body Proyecto proyecto
    );

    //leer todos los proyectos
    @GET("proyectos.json")
    Call<Map<String,Proyecto>> getProyecto(
            @Query("auth") String idToken
    );

    //leer proyecto
    @GET("proyectos/{id}.json")
    Call<Proyecto> getProyecto(
            @Path("id") String id,
            @Query("auth") String idToken
    );

    //Actualizar parcialmente
    @PATCH("proyectos/{is}.json")
    Call<Proyecto> updateProyecto(
            @Path("id")String id,
            @Query("auth") String idToken,
            @Body Map<String,Object> updates
    );

    // Eliminar proyecto
    @DELETE("proyectos/{id}.json")
    Call<Void> deleteProyecto(
            @Path("id") String id,
            @Query("auth") String idToken
    );
}
