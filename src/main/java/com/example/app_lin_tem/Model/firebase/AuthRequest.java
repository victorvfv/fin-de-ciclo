package com.example.app_lin_tem.Model.firebase;

public class AuthRequest {
    public String email;
    public String password;
    public boolean returnSecureToken = true;

    public AuthRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
