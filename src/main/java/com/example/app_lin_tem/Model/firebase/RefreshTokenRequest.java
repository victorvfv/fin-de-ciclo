package com.example.app_lin_tem.Model.firebase;

public class RefreshTokenRequest {
    public String grant_type = "refresh_token";
    public String refresh_token;

    public RefreshTokenRequest(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
