package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText searchInput;
    private RecyclerView recyclerView;
    private Button searchButton;
    private DestinationAdapter adapter;
    private List<Destination> destinationList = new ArrayList<>();
    private int currentPage = 1;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the views
        searchInput = findViewById(R.id.searchInput);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);

        // Set up the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DestinationAdapter(destinationList, this::onDestinationClick);
        recyclerView.setAdapter(adapter);

        // Set up the search button click listener
        searchButton.setOnClickListener(v -> searchDestinations());

        // Initialize DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Setup the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the navigation drawer toggle
        toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up the navigation view
//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(item -> {
//            // Handle navigation item clicks here
//            drawerLayout.closeDrawers();
//            return true;
//        });

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Get header view to access header elements
        View headerView = navigationView.getHeaderView(0);
        ImageView profileImage = headerView.findViewById(R.id.profile_image);
        TextView userEmail = headerView.findViewById(R.id.user_email);

        // Simulating authenticated user data


        String authenticatedEmail = AuthUser.username ; // Replace with actual user data
        userEmail.setText(authenticatedEmail);

        // Set a click listener for the profile image
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity when the image is clicked
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    // Open Main Activity
                    Toast.makeText(MainActivity.this, "You are already in Home Page", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_booked_places) {
                    // Open Booked Places Activity
                    startActivity(new Intent(MainActivity.this, BookedPlacesActivity.class));
                } else if (id == R.id.nav_about) {
                    // Show About Dialog
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));

                } else if (id == R.id.nav_help) {
                    // Show Help Dialog
                    Toast.makeText(MainActivity.this, "Help is on development", Toast.LENGTH_SHORT).show();

                } else if (id == R.id.nav_logout) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                     } else if (id == R.id.nav_exit_app) {
                    // Logout Functionality
                    Toast.makeText(MainActivity.this, "Thank you for being with us.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                drawerLayout.closeDrawers(); // Close the drawer after clicking
                return true;
            }
        });
    }

    private void showAboutDialog() {
        // Use an AlertDialog to show the about information
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("About the App")
                .setMessage("This app is developed by Group One. Compatible with Android 5.0+.")
                .setPositiveButton("OK", null)
                .show();
    }




    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void searchDestinations() {
        String query = searchInput.getText().toString();
        if (!query.isEmpty()) {
            String url = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=" + query + "&format=json";

            // Make the API call for search results
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray searchResults = response.getJSONObject("query").getJSONArray("search");
                                destinationList.clear();  // Clear current list for fresh search results
                                if (searchResults.length() > 0) {
                                    for (int i = 0; i < searchResults.length(); i++) {
                                        JSONObject result = searchResults.getJSONObject(i);
                                        String title = result.getString("title");
                                        fetchImage(title); // Fetch image URL based on title
                                    }
                                } else {
                                    Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                                }
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, error -> {
                if (error.networkResponse != null) {
                    Log.e("API Error", "Error code: " + error.networkResponse.statusCode);
                }
                Log.e("API Error", error.toString());
            }
            );

            Volley.newRequestQueue(this).add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchImage(String title) {
        String url = "https://en.wikipedia.org/w/api.php?action=query&titles=" + title + "&prop=pageimages|info&format=json&piprop=original";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject pages = response.getJSONObject("query").getJSONObject("pages");
                            JSONArray pageIds = pages.names();
                            if (pageIds != null) {
                                for (int i = 0; i < pageIds.length(); i++) {
                                    String pageId = pageIds.getString(i); // Extract pageId
                                    JSONObject page = pages.getJSONObject(pageId);
                                    String imageUrl = page.has("original") ? page.getJSONObject("original").getString("source") : "";
                                    if (!imageUrl.isEmpty()) {
                                        // Add destination with image URL and pageId to the list
                                        destinationList.add(new Destination(title, "Details for " + title, imageUrl, pageId));
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> Log.e("API Error", error.toString())
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void onDestinationClick(Destination destination) {
        Intent intent = new Intent(MainActivity.this, DestinationDetailActivity.class);
        intent.putExtra("name", destination.getName());
        intent.putExtra("details", destination.getDetails());
        intent.putExtra("image", destination.getImageUrl());
        intent.putExtra("pageId", destination.getPageId());
        intent.putExtra("wikiUrl", "https://en.wikipedia.org/wiki/" + destination.getName());
        startActivity(intent);
    }

    private void fetchDestinations(int page) {
        String query = searchInput.getText().toString();  // Get the current search query
        if (!query.isEmpty()) {
            String url = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=" + query
                    + "&format=json&srprop=snippet&sroffset=" + (page - 1) * 10;  // Pagination offset

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray searchResults = response.getJSONObject("query").getJSONArray("search");
                                for (int i = 0; i < searchResults.length(); i++) {
                                    JSONObject result = searchResults.getJSONObject(i);
                                    String title = result.getString("title");
                                    fetchImage(title);  // Fetch image URL based on title
                                }
                                adapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, error -> Log.e("API Error", error.toString())
            );

            Volley.newRequestQueue(this).add(jsonObjectRequest);
        }
    }
}
