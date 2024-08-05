package com.example.safetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

public class MedicalEmergencyActivity extends AppCompatActivity {

    private static final int CALL_PERMISSION_REQUEST_CODE = 1;
    private static final String AMBULANCE_PHONE_NUMBER = "123";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;

    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_emergency);

        Button callAmbulanceButton = findViewById(R.id.callAmbulanceButton);
        Button callEmergencyButton = findViewById(R.id.callEmergencyButton);
        Button showNearbyHospitalsButton = findViewById(R.id.showNearbyHospitalsButton);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        callAmbulanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeEmergencyCall(AMBULANCE_PHONE_NUMBER);
            }
        });

        callEmergencyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emergencyPhoneNumber = "911";
                makeEmergencyCall(emergencyPhoneNumber);
            }
        });

        showNearbyHospitalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchNearbyHospitals();
            }
        });
    }

    private void makeEmergencyCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST_CODE);
        } else {
            startActivity(callIntent);
        }
    }

    private void fetchNearbyHospitals() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    // You have the user's location (latitude and longitude) here.
                    // Now, you can make an API request to fetch nearby hospitals.
                    // Use the Google Places API and handle the response accordingly.
                    LatLng userLocation = new LatLng(latitude, longitude);
                    String nearbyHospitalsUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json" +
                            "?location=" + latitude + "," + longitude +
                            "&radius=5000" +
                            "&types=hospital" +
                            "&key=AIzaSyDGTHIBp5hbEAxJfGdW1rHDUBTuOG5JyY4";

                    // Make a network request to nearbyHospitalsUrl and handle the response
                    // Implement your logic here to display nearby hospitals based on the API response.
                    // You can use Retrofit, Volley, or any other HTTP library for making the API request.
                    // After receiving the response, parse it and display the hospital information.
                    // For simplicity, I'm displaying a toast message here.
                    Toast.makeText(this, "Fetching nearby hospitals for location: " + userLocation, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Call permission granted, make the emergency call
                makeEmergencyCall(AMBULANCE_PHONE_NUMBER);
            } else {
                Toast.makeText(this, "Call permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Location permission is granted, fetch user location
                fetchNearbyHospitals();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}