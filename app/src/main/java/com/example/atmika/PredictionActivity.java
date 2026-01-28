package com.example.atmika;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PredictionActivity extends AppCompatActivity {

    private Spinner locationSpinner, furnishingSpinner;
    private EditText sqftEditText, bedroomsEditText, bathroomsEditText, balconiesEditText, ageEditText;
    private Switch parkingSwitch;
    private Button calculateButton, backToHomeButton;
    private TextView resultTextView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        db = new DatabaseHelper(this);

        locationSpinner = findViewById(R.id.locationSpinner);
        furnishingSpinner = findViewById(R.id.furnishingSpinner);
        sqftEditText = findViewById(R.id.sqftEditText);
        bedroomsEditText = findViewById(R.id.bedroomsEditText);
        bathroomsEditText = findViewById(R.id.bathroomsEditText);
        balconiesEditText = findViewById(R.id.balconiesEditText);
        ageEditText = findViewById(R.id.ageEditText);
        parkingSwitch = findViewById(R.id.parkingSwitch);
        calculateButton = findViewById(R.id.calculateButton);
        resultTextView = findViewById(R.id.resultTextView);
        backToHomeButton = findViewById(R.id.backToHomeButton);

        loadLocations();

        calculateButton.setOnClickListener(v -> calculatePrice());

        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadLocations() {
        List<String> locations = db.getAllLocations();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, locations);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(dataAdapter);
    }

    private void calculatePrice() {
        if (!validateInputs()) {
            return;
        }

        String location = locationSpinner.getSelectedItem().toString();
        double sqft = Double.parseDouble(sqftEditText.getText().toString());
        int bedrooms = Integer.parseInt(bedroomsEditText.getText().toString());
        int bathrooms = Integer.parseInt(bathroomsEditText.getText().toString());
        int balconies = Integer.parseInt(balconiesEditText.getText().toString());
        int age = Integer.parseInt(ageEditText.getText().toString());
        boolean parking = parkingSwitch.isChecked();
        String furnishing = furnishingSpinner.getSelectedItem().toString();

        double locationRate = db.getRateForLocation(location);
        double basePrice = sqft * locationRate;
        double finalPrice = basePrice;

        finalPrice += (bedrooms * 50000);
        finalPrice += (bathrooms * 30000);
        finalPrice += (balconies * 15000);
        if (parking) {
            finalPrice += 200000;
        }
        if (furnishing.equals("Fully Furnished")) {
            finalPrice += 300000;
        } else if (furnishing.equals("Semi Furnished")) {
            finalPrice += 150000;
        }
        finalPrice -= (age * 10000);

        db.addPrediction(location, sqft, bedrooms, bathrooms, balconies, parking ? 1 : 0, furnishing, age, finalPrice);

        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("en", "in"));
        resultTextView.setText("Estimated Price: " + format.format(finalPrice));
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(sqftEditText.getText().toString())) {
            Toast.makeText(this, "Enter Square Feet", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(bedroomsEditText.getText().toString())) {
            Toast.makeText(this, "Enter Number of Bedrooms", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(bathroomsEditText.getText().toString())) {
            Toast.makeText(this, "Enter Number of Bathrooms", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(balconiesEditText.getText().toString())) {
            Toast.makeText(this, "Enter Number of Balconies", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(ageEditText.getText().toString())) {
            Toast.makeText(this, "Enter Property Age", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
