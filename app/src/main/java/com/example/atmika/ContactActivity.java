package com.example.atmika;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ContactActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, messageEditText;
    private Button submitButton;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        db = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        messageEditText = findViewById(R.id.messageEditText);
        submitButton = findViewById(R.id.submitAndLeaveButton);

        submitButton.setOnClickListener(v -> submitContact());
    }

    private void submitContact() {
        if (!validateInputs()) {
            return;
        }

        String name = nameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String message = messageEditText.getText().toString();

        long id = db.addContact(name, email, message);

        if (id != -1) {
            Toast.makeText(this, "Message sent successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, HomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Error sending message!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs() {
        if (TextUtils.isEmpty(nameEditText.getText().toString())) {
            Toast.makeText(this, "Enter Your Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(emailEditText.getText().toString())) {
            Toast.makeText(this, "Enter Your Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.getText().toString()).matches()) {
            Toast.makeText(this, "Enter a valid Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(messageEditText.getText().toString())) {
            Toast.makeText(this, "Enter a Message", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
