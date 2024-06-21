package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private TextView missionNameTextView;
    private TextView launchDateTextView;
    private TextView detailsTextView;
    private TextView articleLinkTextView;
    private TextView wikipediaLinkTextView;
    private Button backButton;

    private SpaceXApi spaceXApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        missionNameTextView = findViewById(R.id.missionNameTextView);
        launchDateTextView = findViewById(R.id.launchDateTextView);
        detailsTextView = findViewById(R.id.detailsTextView);
        articleLinkTextView = findViewById(R.id.articleLinkTextView);
        wikipediaLinkTextView = findViewById(R.id.wikipediaLinkTextView);
        backButton = findViewById(R.id.backButton);

        spaceXApi = new SpaceXApi();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("launch_id")) {
            String launchId = intent.getStringExtra("launch_id");
            Log.d(TAG, "Launch ID: " + launchId); // Log the launch ID
            fetchLaunchDetails(launchId);
        } else {
            Toast.makeText(this, "No launch ID provided", Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(v -> finish());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(DetailActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchLaunchDetails(String launchId) {
        spaceXApi.getLaunchDetails(launchId, new SpaceXApi.LaunchDetailsCallback() {
            @Override
            public void onSuccess(LaunchDetails launchDetails) {
                runOnUiThread(() -> {
                    missionNameTextView.setText(launchDetails.getMissionName());
                    launchDateTextView.setText(launchDetails.getLaunchDate());
                    detailsTextView.setText(launchDetails.getDetails());

                    String articleLink = launchDetails.getLinks().getArticleLink();
                    String wikipediaLink = launchDetails.getLinks().getWikipedia();

                    articleLinkTextView.setText(articleLink);
                    wikipediaLinkTextView.setText(wikipediaLink);

                    articleLinkTextView.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(articleLink));
                        startActivity(browserIntent);
                    });

                    wikipediaLinkTextView.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaLink));
                        startActivity(browserIntent);
                    });
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Failed to fetch launch details: " + errorMessage);
                    Toast.makeText(DetailActivity.this, "Failed to fetch launch details", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
