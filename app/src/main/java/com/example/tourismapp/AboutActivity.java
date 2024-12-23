package com.example.tourismapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends NavParent {


    private TextView appDescriptionTextView;
    private TextView developerInfoTextView;
    private TextView versionInfoTextView;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_about;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize the TextViews
        appDescriptionTextView = findViewById(R.id.appDescriptionTextView);
        developerInfoTextView = findViewById(R.id.developerInfoTextView);
        versionInfoTextView = findViewById(R.id.versionInfoTextView);

        // Set the content for the TextViews
        appDescriptionTextView.setText("Welcome to TourismApp, your go-to guide for exploring top destinations.");
        developerInfoTextView.setText("Developer: Besufkad Mekonnen\nEmail: besumack@tourismApp.com");
        versionInfoTextView.setText("Version: 1.0.0");
    }


}
