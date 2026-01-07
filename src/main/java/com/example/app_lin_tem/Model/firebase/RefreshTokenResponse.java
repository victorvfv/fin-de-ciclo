package com.example.app_lin_tem.Model.firebase;

public class RefreshTokenResponse {
    public String access_token; // nuevo idToken
    public String expires_in;
    public String token_type;
    public String refresh_token; // puede ser el mismo
    public String user_id;
}
