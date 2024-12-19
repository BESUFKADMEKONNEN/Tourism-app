package com.example.tourismapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BookedPlacesAdapter extends RecyclerView.Adapter<BookedPlacesAdapter.ViewHolder> {

    private final List<Destination> bookedPlaces;  // Assuming BookedPlace is the data type for booked places
    private final DestinationClickListener listener;

    // Interface to handle item clicks
    public interface DestinationClickListener {
        void onDestinationClick(Destination destination); // Handling booked place click
    }

    // Constructor for the adapter
    public BookedPlacesAdapter(List<Destination> bookedPlaces, DestinationClickListener listener) {
        this.bookedPlaces = bookedPlaces;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item (item_destination layout in your case)
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booked, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destination bookedPlace = bookedPlaces.get(position);
        holder.name.setText(bookedPlace.getName());
        holder.details.setText(bookedPlace.getDetails());
        Glide.with(holder.itemView.getContext())
                .load(bookedPlace.getImageUrl())  // Assuming BookedPlace has a getImageUrl() method
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> listener.onDestinationClick(bookedPlace));
    }

    @Override
    public int getItemCount() {
        return bookedPlaces.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView details;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.destinationName);  // Same ID as item_destination layout
            details = itemView.findViewById(R.id.destinationDetails); // Same ID as item_destination layout
            imageView = itemView.findViewById(R.id.destinationImage); // Same ID as item_destination layout
        }
    }
}
