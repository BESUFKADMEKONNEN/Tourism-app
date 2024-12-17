package com.example.tourismapp;

public class LoginRequest {
    private String password;

    public LoginRequest(String username, String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Constructor, getters, and setters
}