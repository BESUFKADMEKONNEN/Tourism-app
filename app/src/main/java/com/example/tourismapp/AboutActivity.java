package com.example.tourismapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private TextView appDescriptionTextView;
    private TextView developerInfoTextView;
    private TextView versionInfoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Initialize the TextViews
        appDescriptionTextView = findViewById(R.id.appDescriptionTextView);
        developerInfoTextView = findViewById(R.id.developerInfoTextView);
        versionInfoTextView = findViewById(R.id.versionInfoTextView);

        // Set the content for the TextViews
        appDescriptionTextView.setText("Welcome to TourismApp, your go-to guide for exploring top destinations.");
        developerInfoTextView.setText("Developer: Besufkad Mekonnen\nEmail: besufkad@example.com");
        versionInfoTextView.setText("Version: 1.0.0");
    }
}
