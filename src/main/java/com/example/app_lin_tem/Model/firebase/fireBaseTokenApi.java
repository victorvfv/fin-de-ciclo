package com.example.app_lin_tem.Model.firebase;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface fireBaseTokenApi {
    @POST("token")
    @FormUrlEncoded
    Call<RefreshTokenResponse> refreshToken(
            @Query("key") String apiKey,
            @Field("grant_type") String grant_type,
            @Field("refresh_token") String refresh_token

    );
}
