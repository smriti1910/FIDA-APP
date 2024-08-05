package com.example.safetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class WomenSafetyEmergencyActivity extends AppCompatActivity {

    private static final int CALL_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_womensafety_emergency);

        Button womenSafetyButton = findViewById(R.id.womenSafetyButton);

        womenSafetyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEmergencyCall("Women Safety");
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
        String phoneNumber = null;
        switch (emergencyType) {
            case "Women Safety":
                phoneNumber = "1234567890"; // Replace with the actual women safety phone number
                break;

        }
        return phoneNumber;
    }

    private void reportToPoliceStation() {
        // TODO: Implement the logic to report to the nearby police station

        // Get the current location
        // For this example, let's assume the location is hardcoded
        double latitude = 37.7749; // Replace with the actual latitude
        double longitude = -122.4194; // Replace with the actual longitude

        // Convert the latitude and longitude to an address
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String policeStationAddress = address.getAddressLine(0); // Get the first address line
                // TODO: Send the police station address for reporting or perform any other required action
                Toast.makeText(this, "Police Station Address: " + policeStationAddress, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "No address found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, make the call
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + getPhoneNumberForEmergencyType("Women Safety")));
                startActivity(callIntent);
            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
