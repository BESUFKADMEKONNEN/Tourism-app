package com.example.tourismapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class BookedPlacesAdapter extends RecyclerView.Adapter<BookedPlacesAdapter.ViewHolder> {

    private final List<Destination> bookedPlaces;
    private final DestinationClickListener listener;
    private Context context;

    // Interface to handle item clicks
    public interface DestinationClickListener {
        void onDestinationClick(Destination destination);
    }

    // Constructor for the adapter
    public BookedPlacesAdapter(Context context,List<Destination> bookedPlaces, DestinationClickListener listener) {
        this.bookedPlaces = bookedPlaces;
        this.listener = listener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booked, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destination bookedPlace = bookedPlaces.get(position);
        holder.name.setText(bookedPlace.getName());
        holder.details.setText(bookedPlace.getDetails());
        Glide.with(holder.itemView.getContext())
                .load(bookedPlace.getImageUrl())
                .into(holder.imageView);

        holder.deleteIcon.setOnClickListener(v -> {
            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Delete Confirmation")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteFromFirebase(bookedPlace.getPageId(), position);
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .create()
                    .show();
        });

        holder.itemView.setOnClickListener(v -> listener.onDestinationClick(bookedPlace));
    }

    private void deleteFromFirebase(String pageId, int position) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("bookmarks");
        Query query = databaseReference.orderByChild("pageId").equalTo(pageId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    snapshot.getRef().removeValue()
                            .addOnSuccessListener(aVoid -> {
                                bookedPlaces.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position, bookedPlaces.size());
                                Toast.makeText(context, "Item deleted successfully.", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
                Toast.makeText(context, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
            deleteIcon = itemView.findViewById(R.id.deleteIcon);
        }
    }
}
