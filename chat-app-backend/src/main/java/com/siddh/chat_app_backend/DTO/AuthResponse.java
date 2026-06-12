package com.siddh.chat_app_backend.DTO;

public class AuthResponse {
    String token;
    public AuthResponse(){

    }

    public AuthResponse(String token) {
        this.token = token;
    }
}
