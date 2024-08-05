package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserRegistration extends AppCompatActivity {
        private EditText etFirstName, etLastName, etEmail, etPassword;
        private Button btnRegister;
        private TextView tvLoginRedirect;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_registration);

            etFirstName = findViewById(R.id.etFirstName);
            etLastName = findViewById(R.id.etLastName);
            etEmail = findViewById(R.id.etEmail);

            etPassword = findViewById(R.id.etPassword);
            btnRegister = findViewById(R.id.btnRegister);
            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String firstName = etFirstName.getText().toString().trim();
                    String lastName = etLastName.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String password = etPassword.getText().toString();


                    // TODO: Implement the registration logic

                    // Assuming registration is successful, redirect to login page
                    openLoginPage();
                }
            });
        }

    private void openLoginPage() {
        Intent intent = new Intent(UserRegistration.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Optional: Finish the RegistrationActivity to prevent going back to it
    }
}