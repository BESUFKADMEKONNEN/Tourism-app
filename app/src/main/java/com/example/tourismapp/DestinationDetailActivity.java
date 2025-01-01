package com.example.tourismapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class DestinationDetailActivity extends NavParent {

    private TextView destinationDetails;
    private TextView countryTextView, stateTextView, cityTextView, priceTextView;
    private Button clickMeButton, mapButton, bookButton;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_destination_detail;
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Get data from intent
        String name = getIntent().getStringExtra("name");
        String details = getIntent().getStringExtra("details");
        String imageUrl = getIntent().getStringExtra("image");
        String wikiUrl = getIntent().getStringExtra("wikiUrl");
        String pageId = getIntent().getStringExtra("pageId");
        String userId = AuthUser.userId;



        // Find views
        ImageView destinationImage = findViewById(R.id.destinationImage);
        TextView destinationName = findViewById(R.id.destinationName);
        destinationDetails = findViewById(R.id.destinationDetails);
        countryTextView = findViewById(R.id.countryTextView);
        stateTextView = findViewById(R.id.stateTextView);
        cityTextView = findViewById(R.id.cityTextView);
        priceTextView = findViewById(R.id.priceTextView);

        clickMeButton = findViewById(R.id.clickMeButton);
        mapButton = findViewById(R.id.openMapButton);
        bookButton = findViewById(R.id.bookButton);



        FirebaseApp.initializeApp(DestinationDetailActivity.this);

        destinationName.setText(name);
        destinationDetails.setText(Html.fromHtml(details));
        Glide.with(this).load(imageUrl).into(destinationImage);

//        Log.d("DestinationDetail", "Page ID: " + pageId);
        fetchDestinationDetails(pageId);

        clickMeButton.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikiUrl));
            startActivity(browserIntent);
        });

        mapButton.setOnClickListener(v -> {
            double latitude = getIntent().getDoubleExtra("latitude", 0);
            double longitude = getIntent().getDoubleExtra("longitude", 0);
            Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);

            // Create an Intent to open Google Maps
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            // Check if Google Maps is available to handle the intent
            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                // Notify the user with an alert box
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Google Maps Not Found")
                        .setMessage("Google Maps app is not available on this device. Do you want to open the location in your browser?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // Open the location in the default browser
                            Uri webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=" + latitude + "," + longitude);
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, webUri);

                            if (browserIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(browserIntent);
                            } else {
                                Toast.makeText(this, "No browser available to open the location.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", (dialog, which) -> {
                            // Dismiss the dialog if the user chooses "No"
                            dialog.dismiss();
                        })
                        .show();
            }
        });


        bookButton.setOnClickListener(v -> {
            addBookmark(pageId,name,details,imageUrl,wikiUrl,userId);
           });



       }

    private void fetchDestinationDetails(String pageId) {
        String url = "https://en.wikipedia.org/w/api.php?action=query&pageids=" + pageId + "&prop=extracts|coordinates&exintro=true&explaintext=true&format=json";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Get the 'pages' object
                            JSONObject pages = response.getJSONObject("query").getJSONObject("pages");
                            // Get the page for the given pageId
                            JSONObject page = pages.getJSONObject(pageId);

                            // Extract 'extract' and 'coordinates' from the response
                            String extract = page.optString("extract", "No extract available.");
                            String coordinates = "";

                            if (page.has("coordinates")) {
                                JSONObject coordinatesJson = page.getJSONArray("coordinates").getJSONObject(0);
                                double lat = coordinatesJson.getDouble("lat");
                                double lon = coordinatesJson.getDouble("lon");

                                // Fetch additional location details using Nominatim API
                                fetchLocationDetails(lat, lon);

                                coordinates = "Coordinates: " + lat + ", " + lon;
                            }

                            // Set the details text (extract and coordinates)
                            String detailsText = extract + "\n\n" + coordinates;
                            destinationDetails.setText(detailsText);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DestinationDetailActivity.this, "Failed to load details.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, error -> {
            Log.e("API Error", error.toString());
            Toast.makeText(DestinationDetailActivity.this, "Error fetching details.", Toast.LENGTH_SHORT).show();
        });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void addBookmark(String pageId,String name,String details,String imageUrl,String wikiUrl,String userId) {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bookmarks");

        // Query Firebase for existing bookmarks with the same title (or URL)
        databaseReference.orderByChild("pageId").equalTo(pageId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // If the dataSnapshot is not empty, the bookmark already exists
                if (dataSnapshot.exists()) {
                    // Handle case where bookmark already exists
                    Toast.makeText(DestinationDetailActivity.this, "Bookmark already exists!", Toast.LENGTH_SHORT).show();
                } else {
                    // Add the bookmark if it doesn't exist
                    Bookmark bookmark = new Bookmark(pageId,name,details,imageUrl,wikiUrl,userId); // Add profile image URL if applicable
                    databaseReference.push().setValue(bookmark);
                    Toast.makeText(DestinationDetailActivity.this, "Bookmark added!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error
                Toast.makeText(DestinationDetailActivity.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchLocationDetails(double latitude, double longitude) {
        // Updated URL with accept-language parameter for English results
        String url = "https://nominatim.openstreetmap.org/reverse?lat=" + latitude + "&lon=" + longitude + "&format=json&accept-language=en";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject address = response.getJSONObject("address");

                            // Extract country, state, and city
                            String country = address.optString("country", null); // Set to null if not found
                            String state = address.optString("state", null);   // Set to null if not found
                            String city = address.optString("city", null);     // Set to null if not found

                            // Handle country visibility
                            if (country != null && !country.isEmpty()) {
                                countryTextView.setText("Country: " + country);
                                countryTextView.setVisibility(View.VISIBLE);
                            } else {
                                countryTextView.setVisibility(View.GONE); // Hide if no country found
                            }

                            // Handle state visibility
                            if (state != null && !state.isEmpty()) {
                                stateTextView.setText("State/Province: " + state);
                                stateTextView.setVisibility(View.VISIBLE);
                            } else {
                                stateTextView.setVisibility(View.GONE); // Hide if no state found
                            }

                            // Handle city visibility
                            if (city != null && !city.isEmpty()) {
                                cityTextView.setText("City: " + city);
                                cityTextView.setVisibility(View.VISIBLE);
                            } else {
                                cityTextView.setVisibility(View.GONE); // Hide if no city found
                            }

                            // Calculate and display the price
                            double price = calculatePrice(latitude, longitude, country, state);
                            priceTextView.setText("Price: $" + price);  // Display the price below state

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DestinationDetailActivity.this, "Error fetching location details.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, error -> {
            Log.e("API Error", error.toString());
            Toast.makeText(DestinationDetailActivity.this, "Error fetching location details.", Toast.LENGTH_SHORT).show();
        });

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

     private double calculatePrice(double latitude, double longitude, String country, String state) {
        // Convert latitude and longitude to strings and concatenate them with country and state
        String locationString = latitude + "" + longitude + (country != null ? country : "") + (state != null ? state : "");

        // Calculate the total ASCII value of the location string
        int totalAsciiValue = 0;
        for (int i = 0; i < locationString.length(); i++) {
            totalAsciiValue += locationString.charAt(i);
        }

        // Return the price as a decimal value
        return totalAsciiValue; // Dividing by 1000 to scale down the value
    }
}
