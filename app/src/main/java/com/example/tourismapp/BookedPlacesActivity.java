package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookedPlacesActivity extends NavParent {

    private RecyclerView recyclerView;
    private TextView noBookedPlacesTextView;
    private BookedPlacesAdapter adapter;
    private List<Destination> bookedPlaces;
    private String currentUserId;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_booked_places;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentUserId=AuthUser.userId;
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_booked_places);


        recyclerView = findViewById(R.id.recyclerViewBooked);
        noBookedPlacesTextView = findViewById(R.id.noBookedPlacesTextView);

        // Initialize booked places list
        bookedPlaces = new ArrayList<>();

        // Fetch booked places data from Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bookmarks");
        databaseReference.orderByChild("userId").equalTo(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bookedPlaces.clear(); // Clear previous data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Retrieve each destination from Firebase
                    String name = snapshot.child("name").getValue(String.class);
                    String details = snapshot.child("details").getValue(String.class);
                    String imageUrl = snapshot.child("imageUrl").getValue(String.class);
                    String pageId = snapshot.child("pageId").getValue(String.class);

                    // Create a Destination object
                    Destination destination = new Destination(name, details, imageUrl, pageId);
                    bookedPlaces.add(destination);
                }

                if (!bookedPlaces.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    noBookedPlacesTextView.setVisibility(View.GONE);

                    // Set up RecyclerView with updated data
                    adapter = new BookedPlacesAdapter(BookedPlacesActivity.this, bookedPlaces, destination -> {
                        Intent intent = new Intent(BookedPlacesActivity.this, BookedDetailActivity.class);
                        intent.putExtra("name", destination.getName());
                        intent.putExtra("details", destination.getDetails());
                        intent.putExtra("image", destination.getImageUrl());
                        intent.putExtra("pageId", destination.getPageId());
                        intent.putExtra("wikiUrl", "https://en.wikipedia.org/wiki/" + destination.getName());
                        startActivity(intent);
                    });

                    recyclerView.setLayoutManager(new LinearLayoutManager(BookedPlacesActivity.this));
                    recyclerView.setAdapter(adapter);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noBookedPlacesTextView.setVisibility(View.VISIBLE);
                    noBookedPlacesTextView.setText("No booked places available");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(BookedPlacesActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
