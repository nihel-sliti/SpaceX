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

public class MissionAdapter extends RecyclerView.Adapter<MissionAdapter.ViewHolder> implements Filterable {

    private List<Mission> missions;
    private List<Mission> missionsFiltered;
    private Context context;

    public MissionAdapter(Context context, List<Mission> missions) {
        this.context = context;
        this.missions = missions;
        this.missionsFiltered = missions;
    }

    public void updateMissions(List<Mission> newMissions) {
        this.missions = newMissions;
        this.missionsFiltered = newMissions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_mission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mission mission = missionsFiltered.get(position);
        holder.textMissionName.setText(mission.getMissionName());
        holder.textDescription.setText(mission.getDescription());
        holder.itemView.setOnClickListener(v -> {
            String missionId = mission.getMissionId();
            Log.d("MissionAdapter", "Mission ID: " + missionId);
            if (missionId != null) {
                Intent intent = new Intent(context, MissionDetailActivity.class);
                intent.putExtra("mission_id", missionId);
                context.startActivity(intent);
            } else {
                Log.e("MissionAdapter", "Mission ID is null");
            }
        });
    }

    @Override
    public int getItemCount() {
        return missionsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    missionsFiltered = missions;
                } else {
                    List<Mission> filteredList = new ArrayList<>();
                    for (Mission mission : missions) {
                        if (mission.getMissionName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(mission);
                        }
                    }
                    missionsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = missionsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                missionsFiltered = (ArrayList<Mission>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textMissionName;
        TextView textDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textMissionName = itemView.findViewById(R.id.textMissionName);
            textDescription = itemView.findViewById(R.id.textDescription);
        }
    }
}
