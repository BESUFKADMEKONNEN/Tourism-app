package com.example.tourismapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    Call<User> loginUser(@Body LoginRequest loginRequest);
}