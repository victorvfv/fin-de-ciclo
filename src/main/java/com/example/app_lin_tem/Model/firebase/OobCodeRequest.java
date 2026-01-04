package com.example.app_lin_tem.Model.firebase;

public class OobCodeRequest {
    public String requestType; // "VERIFY_EMAIL" o "PASSWORD_RESET"
    public String idToken;     // necesario para VERIFY_EMAIL
    public String email;       // necesario para PASSWORD_RESET
}
