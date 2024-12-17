package com.example.tourismapp;

public class User {
    public String firstName, lastName, username, gender, password, profileImageUrl;

    // Default constructor required for Firebase data deserialization
    public User() {
    }

    public User(String firstName, String lastName, String gender, String username, String password, String profileImageUrl) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.profileImageUrl = profileImageUrl;
        this.gender = gender;
    }
}
