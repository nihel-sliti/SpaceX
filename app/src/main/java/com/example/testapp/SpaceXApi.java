package com.example.testapp;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SpaceXApi {

    private static final String TAG = "SpaceXApi";
    private static final String BASE_URL = "https://api.spacexdata.com/v3/";

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    public void getLaunches(final LaunchCallback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "launches")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch launches: " + e.getMessage());
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.d(TAG, "Launches JSON: " + json); // Log the JSON response
                    Type listType = new TypeToken<List<Launch>>(){}.getType();
                    List<Launch> launches = gson.fromJson(json, listType);
                    callback.onSuccess(launches);
                } else {
                    Log.e(TAG, "Failed to fetch launches: " + response.code());
                    callback.onFailure("Failed to fetch launches: " + response.code());
                }
            }
        });
    }

    public void getLaunchDetails(String id, final LaunchDetailsCallback callback) {
        String url = BASE_URL + "launches/" + id;
        Log.d(TAG, "Fetching launch details from URL: " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch launch details: " + e.getMessage());
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    LaunchDetails launchDetails = gson.fromJson(json, LaunchDetails.class);
                    callback.onSuccess(launchDetails);
                } else {
                    Log.e(TAG, "Failed to fetch launch details: " + response.code());
                    callback.onFailure("Failed to fetch launch details: " + response.code());
                }
            }
        });
    }

    public void getMissions(final MissionCallback callback) {
        Request request = new Request.Builder()
                .url(BASE_URL + "missions")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch missions: " + e.getMessage());
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    Log.d(TAG, "Missions JSON: " + json); // Log the JSON response
                    Type listType = new TypeToken<List<Mission>>(){}.getType();
                    List<Mission> missions = gson.fromJson(json, listType);
                    callback.onSuccess(missions);
                } else {
                    Log.e(TAG, "Failed to fetch missions: " + response.code());
                    callback.onFailure("Failed to fetch missions: " + response.code());
                }
            }
        });
    }

    public void getMissionDetails(String id, final MissionDetailsCallback callback) {
        String url = BASE_URL + "missions/" + id;
        Log.d(TAG, "Fetching mission details from URL: " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Failed to fetch mission details: " + e.getMessage());
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    MissionDetails missionDetails = gson.fromJson(json, MissionDetails.class);
                    callback.onSuccess(missionDetails);
                } else {
                    Log.e(TAG, "Failed to fetch mission details: " + response.code());
                    callback.onFailure("Failed to fetch mission details: " + response.code());
                }
            }
        });
    }

    public interface LaunchCallback {
        void onSuccess(List<Launch> launches);
        void onFailure(String errorMessage);
    }

    public interface LaunchDetailsCallback {
        void onSuccess(LaunchDetails launchDetails);
        void onFailure(String errorMessage);
    }

    public interface MissionCallback {
        void onSuccess(List<Mission> missions);
        void onFailure(String errorMessage);
    }

    public interface MissionDetailsCallback {
        void onSuccess(MissionDetails missionDetails);
        void onFailure(String errorMessage);
    }
}
