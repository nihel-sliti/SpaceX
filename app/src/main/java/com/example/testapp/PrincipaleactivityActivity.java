package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class PrincipaleactivityActivity extends AppCompatActivity {
    private Button btnLaunches;
    private Button btnMissions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.principaleactivity);

        btnLaunches = findViewById(R.id.btnLaunches);
        btnMissions = findViewById(R.id.btnMissions);

        btnLaunches.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipaleactivityActivity.this, MainActivity.class);
            startActivity(intent);
        });

        btnMissions.setOnClickListener(v -> {
            Intent intent = new Intent(PrincipaleactivityActivity.this, MissonRecycle.class);
            startActivity(intent);
        });
    }
}