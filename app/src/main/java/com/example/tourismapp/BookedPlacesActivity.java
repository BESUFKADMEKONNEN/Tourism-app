package com.example.tourismapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BookedPlacesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView noBookedPlacesTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booked_places);

        recyclerView = findViewById(R.id.recyclerViewBooked);
        noBookedPlacesTextView = findViewById(R.id.noBookedPlacesTextView);

        // Retrieve the list of booked places
        List<Destination> bookedPlaces = getIntent().getParcelableArrayListExtra("bookedPlaces");

        // Handle the case where there are no booked places
        if (bookedPlaces != null && !bookedPlaces.isEmpty()) {
            // Show the RecyclerView and hide the "No booked places" message
            recyclerView.setVisibility(View.VISIBLE);
            noBookedPlacesTextView.setVisibility(View.GONE);

            // Set up the adapter with the list of booked places
            DestinationAdapter adapter = new DestinationAdapter(bookedPlaces, destination -> {
                // Handle click event here
                Toast.makeText(this, "Clicked on " + destination.getName(), Toast.LENGTH_SHORT).show();
                // You can also open a new activity to show detailed info about the destination
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } else {
            // No booked places available, show the message
            recyclerView.setVisibility(View.GONE);
            noBookedPlacesTextView.setVisibility(View.VISIBLE);
            noBookedPlacesTextView.setText("No booked places available");
        }
    }
}
