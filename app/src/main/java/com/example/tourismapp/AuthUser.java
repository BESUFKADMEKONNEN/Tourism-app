package com.example.tourismapp;

import android.util.Base64;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AuthUser {
    public static String username, firstName, lastName, password, gender, userId, profileImage;

    public AuthUser() {
    }

    public AuthUser(String firstName, String lastName, String gender, String username, String password, String userId, String profileImageUri) {
        AuthUser.firstName = firstName;
        AuthUser.lastName = lastName;
        AuthUser.gender = gender;
        AuthUser.username = username;
        AuthUser.password = password;
        AuthUser.userId = userId;
        AuthUser.profileImage = profileImageUri;
    }

    public static boolean updateUserDetails(String userId, String firstName, String lastName, String gender, String username, String password, String profileImage) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        // Check if the profileImage needs encoding
        String encodedImage = encodeImage(profileImage);

        // Create a HashMap to store the updated user details
        HashMap<String, Object> updates = new HashMap<>();
        updates.put("firstName", firstName);
        updates.put("lastName", lastName);
        updates.put("gender", gender);
        updates.put("username", username);
        updates.put("password", password);
        updates.put("profileImage", encodedImage);

        // Perform the update
        databaseReference.updateChildren(updates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        AuthUser.firstName = firstName;
                        AuthUser.lastName = lastName;
                        AuthUser.gender = gender;
                        AuthUser.username = username;
                        AuthUser.password = password;
                        AuthUser.profileImage = encodedImage;

                        Log.d("Firebase", "User details updated successfully");
                    } else {
                        Log.e("Firebase", "Failed to update user details", task.getException());
                    }
                });
        return true;
    }

    private static String encodeImage(String imagePath) {
        try {
            // Convert the image at the given path to a byte array (replace with your actual image loading logic)
            byte[] imageBytes = imagePath.getBytes(); // Replace with actual image bytes if available
            return Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            Log.e("Encoding", "Failed to encode image", e);
            return null;
        }
    }

    public static String decodeImage(String encodedImage) {
        try {
            byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return new String(decodedBytes); // Use this string representation if applicable
        } catch (Exception e) {
            Log.e("Decoding", "Failed to decode image", e);
            return null;
        }
    }
}
