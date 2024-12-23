package com.example.tourismapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends NavParent {

    private EditText searchInput;
    private RecyclerView recyclerView;
    private Button searchButton;
    private DestinationAdapter adapter;
    private List<Destination> destinationList = new ArrayList<>();
    private int currentPage = 1;

    private ImageView profileImage;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

    }


    private void searchDestinations() {
        String query = searchInput.getText().toString();
        if (!query.isEmpty()) {
            String url = "https://en.wikipedia.org/w/api.php?action=query&list=search&srsearch=" + query + "&format=json";

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    response -> {
                        try {
                            JSONArray searchResults = response.getJSONObject("query").getJSONArray("search");
                            destinationList.clear();
                            if (searchResults.length() > 0) {
                                for (int i = 0; i < searchResults.length(); i++) {
                                    JSONObject result = searchResults.getJSONObject(i);
                                    String title = result.getString("title");
                                    fetchImage(title);
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "No results found", Toast.LENGTH_SHORT).show();
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.e("API Error", error.toString())
            );

            Volley.newRequestQueue(this).add(jsonObjectRequest);
        } else {
            Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchImage(String title) {
        String url = "https://en.wikipedia.org/w/api.php?action=query&titles=" + title + "&prop=pageimages|info&format=json&piprop=original";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONObject pages = response.getJSONObject("query").getJSONObject("pages");
                        JSONArray pageIds = pages.names();
                        if (pageIds != null) {
                            for (int i = 0; i < pageIds.length(); i++) {
                                String pageId = pageIds.getString(i);
                                JSONObject page = pages.getJSONObject(pageId);
                                String imageUrl = page.optJSONObject("thumbnail") != null ?
                                        page.getJSONObject("thumbnail").getString("source") : null;
                                if (!imageUrl.isEmpty()) {
                                    // Add destination with image URL and pageId to the list
                                    destinationList.add(new Destination(title, "Details for " + title, imageUrl, pageId));
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("API Error", error.toString())
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void onDestinationClick(Destination destination) {
        Intent intent = new Intent(MainActivity.this, DestinationDetailActivity.class);
        intent.putExtra("destination", destination);
        startActivity(intent);
    }
}
