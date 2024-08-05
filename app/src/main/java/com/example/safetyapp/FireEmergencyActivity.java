
package com.example.safetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class FireEmergencyActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private static final int CALL_PERMISSION_REQUEST_CODE = 1;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_emergency);

        Button fireButton = findViewById(R.id.fireButton);

        // Initialize TextToSpeech
        textToSpeech = new TextToSpeech(this, this);

        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndMakeEmergencyCall();
            }
        });
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TextToSpeech", "Language not supported");
            }
        } else {
            Log.e("TextToSpeech", "Initialization failed");
        }
    }

    private void checkAndMakeEmergencyCall() {
        // Check if CALL_PHONE permission is granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {
            makeEmergencyCall("Fire");
        } else {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PERMISSION_REQUEST_CODE);
        }
    }

    private void makeEmergencyCall(String emergencyType) {
        String phoneNumber = getPhoneNumberForEmergencyType(emergencyType);

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);

            String assistanceMessage =  "Smriti is approaching you for help.";
            speak(assistanceMessage);
        } else {
            Toast.makeText(this, "Emergency phone number not available", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPhoneNumberForEmergencyType(String emergencyType) {
        // TODO: Implement the logic to fetch the phone number based on the emergency type
        // Replace the return statement with your actual implementation
        if (emergencyType.equals("Fire")) {
            return "9363076252"; // Replace with the actual fire station phone number
        }
        return null;
    }

    private String getUsername() {
        // TODO: Implement logic to fetch the username during login
        // Return the actual username or an empty string if not available
        return "SMRITI";
    }

    private void speak(String text) {
        Log.d("TextToSpeech", "Speaking: " + text);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, make the call
                makeEmergencyCall("Fire");
            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        // Shutdown TextToSpeech engine when the activity is destroyed
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}