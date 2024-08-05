package com.example.safetyapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

public class EmergencyList extends AppCompatActivity {

    private static final int SEND_SMS_PERMISSION_REQUEST_CODE = 1;
    private static final int CONTACT_PICKER_REQUEST_CODE = 2;
    private static final String SOS_MESSAGE = "I am in danger! Please help me!";

    private GridView emergencyGridView;
    private EmergencyGridAdapter emergencyAdapter;
    private List<String> emergencyContactNumbers = new ArrayList<>();

        public void openGoogleMaps(View view) {
            Uri gmmIntentUri = Uri.parse("geo:0,0");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");

            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Toast.makeText(this, "Google Maps is not installed.", Toast.LENGTH_SHORT).show();
            }
        }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency);

        emergencyGridView = findViewById(R.id.emergencyGridView);
        Button sosButton = findViewById(R.id.sosButton);
        Button addContactsButton = findViewById(R.id.addContactsButton);
        Button viewContactsButton = findViewById(R.id.viewContactsButton);

        List<String> emergencyList = getEmergencyScenarios();
        int[] imageIds = {
                R.drawable.img,
                R.drawable.img_1,
                R.drawable.img_2,
                R.drawable.img_3,
                R.drawable.img_4,
                R.drawable.img_5,
                R.drawable.img_6
        };

        emergencyAdapter = new EmergencyGridAdapter(this, emergencyList, imageIds);
        emergencyGridView.setAdapter(emergencyAdapter);

        emergencyGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedEmergency = emergencyList.get(position);
                handleEmergencySelection(selectedEmergency);
            }
        });

        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emergencyContactNumbers.isEmpty()) {
                    Toast.makeText(EmergencyList.this, "Please add emergency contacts first", Toast.LENGTH_SHORT).show();
                } else {
                    getLocationAndSendSOSMessage();
                }
            }
        });

        addContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openContactsPicker();
            }
        });

        viewContactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewEmergencyContacts();
            }
        });
    }

    private void openContactsPicker() {
        Intent contactsIntent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(contactsIntent, CONTACT_PICKER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                Uri contactData = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                Cursor cursor = getContentResolver().query(contactData, projection, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phoneNumber = cursor.getString(phoneIndex);
                    cursor.close();
                    addEmergencyContact(phoneNumber);
                } else {
                    Toast.makeText(this, "Failed to retrieve contact", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void addEmergencyContact(String phoneNumber) {
        if (!emergencyContactNumbers.contains(phoneNumber)) {
            emergencyContactNumbers.add(phoneNumber);
            Toast.makeText(this, "Emergency contact added: " + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Emergency contact already added", Toast.LENGTH_SHORT).show();
        }
    }

    private void getLocationAndSendSOSMessage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED) {
            // Request location and send SOS message with location.
            getLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},
                    SEND_SMS_PERMISSION_REQUEST_CODE);
        }
    }

    private void getLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        // Create a Google Maps link with the latitude and longitude.
                        String mapsLink = "https://www.google.com/maps?q=" + latitude + "," + longitude;

                        // Include the Google Maps link in your SOS message.
                        String sosMessageWithLocation = SOS_MESSAGE + "\nLocation: " + mapsLink;

                        sendSOSMessage(sosMessageWithLocation);
                    } else {
                        Toast.makeText(this, "Unable to get location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, e -> {
                    Toast.makeText(this, "Error getting location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void sendSOSMessage(String message) {
        SmsManager smsManager = SmsManager.getDefault();
        for (String phoneNumber : emergencyContactNumbers) {
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
        }
        Toast.makeText(this, "SOS message sent to emergency contacts", Toast.LENGTH_SHORT).show();
    }




    private List<String> getEmergencyScenarios() {
        List<String> scenarios = new ArrayList<>();
        scenarios.add("Fire");
        scenarios.add("Flood");
        scenarios.add("Women Safety");
        scenarios.add("Child Safety");
        scenarios.add("Theft / Robbery");
        scenarios.add("Animal Distress");
        scenarios.add("Medical Emergency");

        return scenarios;
    }

    private void handleEmergencySelection(String selectedEmergency) {
        switch (selectedEmergency) {
            case "Fire":
                // Redirect to FireEmergencyActivity
                Intent fireIntent = new Intent(EmergencyList.this, FireEmergencyActivity.class);
                startActivity(fireIntent);
                break;
            case "Flood":
                // Redirect to FloodEmergencyActivity
                Intent floodIntent = new Intent(EmergencyList.this, FloodEmergencyActivity.class);
                startActivity(floodIntent);
                break;
            case "Women Safety":
                // Redirect to WomenSafetyEmergencyActivity
                Intent womenSafetyIntent = new Intent(EmergencyList.this, WomenSafetyEmergencyActivity.class);
                startActivity(womenSafetyIntent);
                break;
            case "Child Safety":
                // Redirect to ChildSafetyEmergencyActivity
                Intent childSafetyIntent = new Intent(EmergencyList.this, ChildSafetyEmergency.class);
                startActivity(childSafetyIntent);
                break;
            case "Theft / Robbery":
                // Redirect to TheftEmergencyActivity
                Intent theftIntent = new Intent(EmergencyList.this, TheftEmergencyActivity.class);
                startActivity(theftIntent);
                break;
            case "Animal Distress":
                // Redirect to AnimalDistressEmergencyActivity
                Intent distressIntent = new Intent(EmergencyList.this, AnimalDistressActivity.class);
                startActivity(distressIntent);
                break;
            case "Medical Emergency":
                // Redirect to MedicalEmergencyActivity
                Intent medicalIntent = new Intent(EmergencyList.this, MedicalEmergencyActivity.class);
                startActivity(medicalIntent);
                break;


            default:
                Toast.makeText(this, "Unknown emergency scenario", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void viewEmergencyContacts() {
        Intent contactsIntent = new Intent(this, EmergencyContactsActivity.class);
        contactsIntent.putStringArrayListExtra("emergencyContacts", new ArrayList<>(emergencyContactNumbers));
        startActivity(contactsIntent);
    }
}