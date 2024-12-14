package com.example.tourismapp;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WikipediaApi {
    @GET("w/api.php?action=query&prop=extracts|coordinates|info|categories&exsentences=3&explaintext=true&format=json")
    Call<JsonObject> getPageDetails(@Query("pageids") String pageId);
}

