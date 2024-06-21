package com.example.testapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MissionDetailActivity extends AppCompatActivity {

    private static final String TAG = "MissionDetailActivity";
    private TextView missionNameTextView;
    private TextView descriptionTextView;
    private TextView manufacturersTextView;
    private TextView payloadsTextView;
    private TextView wikipediaLinkTextView;
    private TextView websiteLinkTextView;
    private TextView twitterLinkTextView;
    private Button backButton;

    private SpaceXApi spaceXApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission_detail);

        missionNameTextView = findViewById(R.id.missionNameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        manufacturersTextView = findViewById(R.id.manufacturersTextView);
        payloadsTextView = findViewById(R.id.payloadsTextView);
        wikipediaLinkTextView = findViewById(R.id.wikipediaLinkTextView);
        websiteLinkTextView = findViewById(R.id.websiteLinkTextView);
        twitterLinkTextView = findViewById(R.id.twitterLinkTextView);
        backButton = findViewById(R.id.backButton);

        spaceXApi = new SpaceXApi();

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("mission_id")) {
            String missionId = intent.getStringExtra("mission_id");
            Log.d(TAG, "Mission ID: " + missionId);
            fetchMissionDetails(missionId);
        } else {
            Toast.makeText(this, "No mission ID provided", Toast.LENGTH_SHORT).show();
        }

        backButton.setOnClickListener(v -> finish());
    }

    private void fetchMissionDetails(String missionId) {
        spaceXApi.getMissionDetails(missionId, new SpaceXApi.MissionDetailsCallback() {
            @Override
            public void onSuccess(MissionDetails missionDetails) {
                runOnUiThread(() -> {
                    missionNameTextView.setText(missionDetails.getMissionName());
                    descriptionTextView.setText(missionDetails.getDescription());
                    manufacturersTextView.setText("Manufacturers: " + String.join(", ", missionDetails.getManufacturers()));
                    payloadsTextView.setText("Payload IDs: " + String.join(", ", missionDetails.getPayloadIds()));

                    String wikipediaLink = missionDetails.getWikipedia();
                    String websiteLink = missionDetails.getWebsite();
                    String twitterLink = missionDetails.getTwitter();

                    wikipediaLinkTextView.setText(wikipediaLink);
                    websiteLinkTextView.setText(websiteLink);
                    twitterLinkTextView.setText(twitterLink);

                    wikipediaLinkTextView.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(wikipediaLink));
                        startActivity(browserIntent);
                    });

                    websiteLinkTextView.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteLink));
                        startActivity(browserIntent);
                    });

                    twitterLinkTextView.setOnClickListener(v -> {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterLink));
                        startActivity(browserIntent);
                    });
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    Log.e(TAG, "Failed to fetch mission details: " + errorMessage);
                    Toast.makeText(MissionDetailActivity.this, "Failed to fetch mission details", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}
