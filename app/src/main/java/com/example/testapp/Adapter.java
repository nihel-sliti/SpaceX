package com.example.testapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> implements Filterable {

    private List<Launch> launches;
    private List<Launch> launchesFull; // Copy of the original list for filtering
    private Context context;

    public Adapter(Context context, List<Launch> launches) {
        this.context = context;
        this.launches = launches != null ? launches : new ArrayList<>();
        this.launchesFull = new ArrayList<>(this.launches); // Initialize with the full list
    }

    public void updateLaunches(List<Launch> newLaunches) {
        this.launches.clear();
        this.launches.addAll(newLaunches);
        this.launchesFull = new ArrayList<>(newLaunches); // Update the full list as well
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Launch launch = launches.get(position);
        holder.textMissionName.setText(launch.getMissionName());
        holder.textLaunchDate.setText(launch.getLaunchDate());
        holder.itemView.setOnClickListener(v -> {
            String launchId = launch.getFlightNumber();
            Log.d("LaunchAdapter", "Launch ID: " + launchId); // Log the launch ID
            if (launchId != null) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("launch_id", launchId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Log.e("LaunchAdapter", "Launch ID is null");
            }
        });
    }

    @Override
    public int getItemCount() {
        return launches.size();
    }

    @Override
    public Filter getFilter() {
        return launchFilter;
    }

    private Filter launchFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Launch> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(launchesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Launch item : launchesFull) {
                    if (item.getMissionName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            launches.clear();
            launches.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textMissionName;
        TextView textLaunchDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMissionName = itemView.findViewById(R.id.textMissionName);
            textLaunchDate = itemView.findViewById(R.id.textLaunchDate);
        }
    }
}
