package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class BookedPlacesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noBookedPlacesTextView;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_places);

        // Initialize Toolbar
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
                    startActivity(new Intent(BookedPlacesActivity.this, MainActivity.class));
                } else if (id == R.id.nav_booked_places) {
                    Toast.makeText(BookedPlacesActivity.this, "You are already in Booked Places Page", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_about) {
                    startActivity(new Intent(BookedPlacesActivity.this, AboutActivity.class));
                } else if (id == R.id.nav_help) {
                    Toast.makeText(BookedPlacesActivity.this, "Help is on development", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(BookedPlacesActivity.this, LoginActivity.class));
                } else if (id == R.id.nav_exit_app) {
                    Toast.makeText(BookedPlacesActivity.this, "Thank you for being with us.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });

        recyclerView = findViewById(R.id.recyclerViewBooked);
        noBookedPlacesTextView = findViewById(R.id.noBookedPlacesTextView);

        // Retrieve the list of booked places
        List<Destination> bookedPlaces = getIntent().getParcelableArrayListExtra("bookedPlaces");

        if (bookedPlaces != null && !bookedPlaces.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            noBookedPlacesTextView.setVisibility(View.GONE);

            DestinationAdapter adapter = new DestinationAdapter(bookedPlaces, destination -> {
                Toast.makeText(this, "Clicked on " + destination.getName(), Toast.LENGTH_SHORT).show();
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setVisibility(View.GONE);
            noBookedPlacesTextView.setVisibility(View.VISIBLE);
            noBookedPlacesTextView.setText("No booked places available");
        }
    }
}
