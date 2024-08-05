package com.example.safetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FloodEmergencyActivity extends AppCompatActivity {

    private static final int CALL_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flood_emergency);


        Button floodButton = findViewById(R.id.floodButton);



        floodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEmergencyCall("Flood");
            }
        });
    }

    private void makeEmergencyCall(String emergencyType) {
        String phoneNumber = getPhoneNumberForEmergencyType(emergencyType);

        if (phoneNumber != null && !phoneNumber.isEmpty()) {
            // Check if the CALL_PHONE permission is granted
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, make the call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            } else {
                // Permission is not granted, request the permission
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},
                        CALL_PERMISSION_REQUEST_CODE);
            }
        } else {
            Toast.makeText(this, "Emergency phone number not available", Toast.LENGTH_SHORT).show();
        }
    }

    private String getPhoneNumberForEmergencyType(String emergencyType) {
        // TODO: Implement the logic to fetch the phone number based on the emergency type
        // Replace the return statement with your actual implementation
         if (emergencyType.equals("Flood")) {
            return "12345678"; // Replace with the actual flood emergency phone number
        }
        return null;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, make the call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getPhoneNumberForEmergencyType("Fire")));
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
