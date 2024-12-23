package com.example.tourismapp;

import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AuthUser {
    public static String username, firstName, lastName, password, gender, userId, profileImageUrl;

    public AuthUser() {
    }

    public AuthUser(String firstName, String lastName, String gender, String username, String password, String userId, String profileImageUri) {
        AuthUser.firstName = firstName;
        AuthUser.lastName = lastName;
        AuthUser.gender = gender;
        AuthUser.username = username;
        AuthUser.password = password;
        AuthUser.userId = userId;
        AuthUser.profileImageUrl = profileImageUri;
    }

}
