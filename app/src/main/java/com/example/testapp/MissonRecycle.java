package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MissonRecycle extends AppCompatActivity {

    private static final String TAG = "MissionRecycle";
    private static final String PREFS_NAME = "SpaceXPrefs";
    private static final String MISSIONS_KEY = "Missions";

    private RecyclerView recyclerView;
    private MissionAdapter adapter;
    private SpaceXApi spaceXApi;
    private SearchView searchView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misson_recycle);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MissionAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        spaceXApi = new SpaceXApi();

        if (NetworkUtils.isNetworkAvailable(this)) {
            fetchMissions();
        } else {
            loadCachedMissions();
            showOfflineMessage();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        backButton.setOnClickListener(v -> finish());
    }

    private void fetchMissions() {
        spaceXApi.getMissions(new SpaceXApi.MissionCallback() {
            @Override
            public void onSuccess(List<Mission> missions) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Number of missions fetched: " + missions.size());
                    adapter.updateMissions(missions);
                    cacheMissions(missions);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Failed to fetch missions: " + errorMessage);
                    loadCachedMissions();
                    showFetchErrorMessage();
                });
            }
        });
    }

    private void cacheMissions(List<Mission> missions) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(missions);
        editor.putString(MISSIONS_KEY, json);
        editor.apply();
    }

    private void loadCachedMissions() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(MISSIONS_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Mission>>() {}.getType();
            List<Mission> missions = gson.fromJson(json, listType);
            adapter.updateMissions(missions);
        } else {
            Log.d(TAG, "No cached missions available.");
        }
    }

    private void showOfflineMessage() {
        Snackbar.make(recyclerView, "You are offline. Displaying cached data.", Snackbar.LENGTH_LONG).show();
    }

    private void showFetchErrorMessage() {
        Snackbar.make(recyclerView, "Failed to fetch data. Please try again later.", Snackbar.LENGTH_LONG).show();
    }
}
