package com.example.tourismapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

        holder.deleteIcon.setOnClickListener(v -> {
            // Show confirmation dialog
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Perform delete operation
                        bookedPlaces.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, bookedPlaces.size());
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Dismiss the dialog
                        dialog.dismiss();
                    })
                    .create()
                    .show();
        });

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
        public ImageView deleteIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.destinationName);
            details = itemView.findViewById(R.id.destinationDetails);
            imageView = itemView.findViewById(R.id.destinationImage);
            deleteIcon = itemView.findViewById(R.id.deleteIcon); // Add deleteIcon

        }
    }
}
