package com.example.testapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String PREFS_NAME = "SpaceXPrefs";
    private static final String LAUNCHES_KEY = "Launches";

    private RecyclerView recyclerView;
    private Adapter adapter;
    private SpaceXApi spaceXApi;
    private SearchView searchView;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.backButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        spaceXApi = new SpaceXApi();

        if (NetworkUtils.isNetworkAvailable(this)) {
            fetchLaunches();
        } else {
            loadCachedLaunches();
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

    private void fetchLaunches() {
        spaceXApi.getLaunches(new SpaceXApi.LaunchCallback() {
            @Override
            public void onSuccess(List<Launch> launches) {
                runOnUiThread(() -> {
                    Log.d(TAG, "Number of launches fetched: " + launches.size());
                    adapter.updateLaunches(launches);
                    cacheLaunches(launches);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Failed to fetch launches: " + errorMessage);
                    loadCachedLaunches();
                    showFetchErrorMessage();
                });
            }
        });
    }

    private void cacheLaunches(List<Launch> launches) {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(launches);
        editor.putString(LAUNCHES_KEY, json);
        editor.apply();
    }

    private void loadCachedLaunches() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(LAUNCHES_KEY, null);
        if (json != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Launch>>() {}.getType();
            List<Launch> launches = gson.fromJson(json, listType);
            adapter.updateLaunches(launches);
        } else {
            Log.d(TAG, "No cached launches available.");
        }
    }

    private void showOfflineMessage() {
        Snackbar.make(recyclerView, "You are offline. Displaying cached data.", Snackbar.LENGTH_LONG).show();
    }

    private void showFetchErrorMessage() {
        Snackbar.make(recyclerView, "Failed to fetch data. Please try again later.", Snackbar.LENGTH_LONG).show();
    }
}
