package com.example.atmika;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button predictButton = findViewById(R.id.predictButton);
        Button historyButton = findViewById(R.id.historyButton);
        Button aboutButton = findViewById(R.id.aboutButton);
        Button contactButton = findViewById(R.id.contactButton);

        predictButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, PredictionActivity.class)));

        historyButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, HistoryActivity.class)));

        aboutButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, AboutActivity.class)));

        contactButton.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ContactActivity.class)));
    }
}
