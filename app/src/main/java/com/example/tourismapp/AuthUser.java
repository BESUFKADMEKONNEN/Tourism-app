package com.example.tourismapp;

import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AuthUser {
   public static String username, firstName, lastName, password,gender,userId,profileImage;


    public AuthUser() {
    }

    public AuthUser(String firstName,String lastName,String gender,String username,String password, String userId,String profileImageUri) {
        AuthUser.firstName =firstName;
        AuthUser.lastName =lastName;
        AuthUser.gender =gender;
        AuthUser.username = username;
        AuthUser.password =password;
        AuthUser.userId = userId;
        AuthUser.profileImage=profileImageUri;
    }

    public static boolean updateUserDetails(String userId, String firstName, String lastName, String gender, String username, String password) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Create a HashMap to store the updated user details
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("firstName", firstName);
        updates.put("lastName", lastName);
        updates.put("gender", gender);
        updates.put("username", username);
        updates.put("password", password); // Be cautious about storing passwords in plaintext

        // Perform the update
        databaseReference.updateChildren(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AuthUser.firstName =firstName;
                        AuthUser.lastName =lastName;
                        AuthUser.gender =gender;
                        AuthUser.username = username;
                        AuthUser.password =password;
                        Log.d("Firebase", "User details updated successfully");
                    } else {
                        Log.e("Firebase", "Failed to update user details", task.getException());
                    }
                });
        return true;
    }


}
