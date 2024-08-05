package com.example.safetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.etEmail);
        passwordEditText = findViewById(R.id.etPassword);
        loginButton = findViewById(R.id.loginbtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // TODO: Implement the login logic
                boolean loginSuccessful = performLogin(username, password);

                if (loginSuccessful) {
                    openEmergencyListPage();
                } else {
                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean performLogin(String username,String password) {

        if (usernameEditText.length() == 0) {
            usernameEditText.setError("Email is required");
            return false;
        }

        if (passwordEditText.length() == 0) {
            passwordEditText.setError("Password is required");
            return false;
        } else if (passwordEditText.length() < 8) {
            passwordEditText.setError("Password must be minimum 8 characters");
            return false;
        }

        // after all validation return true.
        return true;
    }



  //  private boolean performLogin(String username, String password) {
      //  if (username.equals(usernameEditText) && password.equals(passwordEditText)) {
        //    return true; // Login successful
        //} else {
        //    return false; // Login unsuccessful
        //}

    //}

    private void openEmergencyListPage() {
        Intent intent = new Intent(LoginActivity.this, EmergencyList.class);
        startActivity(intent);
        finish(); // Optional: Finish the LoginActivity to prevent going back to it
    }
}
