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

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    private final List<Destination> destinations;
    private final DestinationClickListener listener;

    public interface DestinationClickListener {
        void onDestinationClick(Destination destination);
    }

    public DestinationAdapter(List<Destination> destinations, DestinationClickListener listener) {
        this.destinations = destinations;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_destination, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destination destination = destinations.get(position);
        holder.name.setText(destination.getName());
        holder.details.setText(destination.getDetails());
        Glide.with(holder.itemView.getContext())
                .load(destination.getImageUrl())
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> listener.onDestinationClick(destination));
    }

    @Override
    public int getItemCount() {
        return destinations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView details;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.destinationName);
            details = itemView.findViewById(R.id.destinationDetails);
            imageView = itemView.findViewById(R.id.destinationImage);
        }
    }
}
