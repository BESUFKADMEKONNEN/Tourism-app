package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AboutActivity extends AppCompatActivity {

    private TextView appDescriptionTextView;
    private TextView developerInfoTextView;
    private TextView versionInfoTextView;
    private DrawerLayout drawerLayout;

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialize DrawerLayout and ActionBarDrawerToggle
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Initialize NavigationView
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    startActivity(new Intent(AboutActivity.this, MainActivity.class));
                } else if (id == R.id.nav_booked_places) {
                    startActivity(new Intent(AboutActivity.this, BookedPlacesActivity.class));

                     } else if (id == R.id.nav_about) {
                    Toast.makeText(AboutActivity.this, "You are already in About", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_help) {
                    Toast.makeText(AboutActivity.this, "Help is on development", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(AboutActivity.this, LoginActivity.class));
                } else if (id == R.id.nav_exit_app) {
                    Toast.makeText(AboutActivity.this, "Thank you for being with us.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

    }
}
