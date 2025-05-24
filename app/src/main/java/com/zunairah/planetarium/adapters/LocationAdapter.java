package com.zunairah.planetarium.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zunairah.planetarium.R;
import com.zunairah.planetarium.models.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> implements Filterable {
    private List<Location> locations;
    private List<Location> locationsFiltered;
    private OnLocationClickListener listener;

    public interface OnLocationClickListener {
        void onLocationClick(Location location);
    }

    public LocationAdapter(List<Location> locations) {
        this.locations = locations;
        this.locationsFiltered = new ArrayList<>(locations);
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = locationsFiltered.get(position);
        holder.bind(location);
    }

    @Override
    public int getItemCount() {
        return locationsFiltered.size();
    }

    public void setOnLocationClickListener(OnLocationClickListener listener) {
        this.listener = listener;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint == null ? "" : constraint.toString().toLowerCase().trim();

                List<Location> filteredList = new ArrayList<>();
                if (query.isEmpty()) {
                    filteredList.addAll(locations);
                } else {
                    for (Location location : locations) {
                        if (location.getName().toLowerCase().contains(query)) {
                            filteredList.add(location);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                locationsFiltered = (List<Location>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView textName;
        private TextView textCoordinates;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_location_name);
            textCoordinates = itemView.findViewById(R.id.text_coordinates);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onLocationClick(locationsFiltered.get(position));
                    }
                }
            });
        }

        public void bind(Location location) {
            textName.setText(location.getName());
            textCoordinates.setText(String.format("%.2f°, %.2f°",
                    location.getLatitude(), location.getLongitude()));
        }
    }
}
