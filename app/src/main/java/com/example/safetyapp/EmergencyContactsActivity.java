package com.example.safetyapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EmergencyContactsActivity extends AppCompatActivity {

    private ListView emergencyContactsListView;
    private ArrayAdapter<String> contactsAdapter;
    private ArrayList<String> emergencyContactNumbers;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contacts);

        emergencyContactsListView = findViewById(R.id.emergencyContactsListView);
        Button removeContactsButton = findViewById(R.id.removeContactsButton);

        emergencyContactNumbers = getIntent().getStringArrayListExtra("emergencyContacts");

        if (emergencyContactNumbers != null && !emergencyContactNumbers.isEmpty()) {
            contactsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, emergencyContactNumbers);
            emergencyContactsListView.setAdapter(contactsAdapter);
            setupRemoveContactListener();
        } else {
            Toast.makeText(this, "No emergency contacts found", Toast.LENGTH_SHORT).show();
        }

        removeContactsButton.setOnClickListener(v -> {
            if (emergencyContactNumbers != null && !emergencyContactNumbers.isEmpty()) {
                removeEmergencyContacts();
            } else {
                Toast.makeText(this, "No emergency contacts to remove", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRemoveContactListener() {
        emergencyContactsListView.setOnItemClickListener((parent, view, position, id) -> {
            String phoneNumber = emergencyContactNumbers.get(position);
            removeEmergencyContact(phoneNumber);
        });
    }

    private void removeEmergencyContact(String phoneNumber) {
        if (emergencyContactNumbers.remove(phoneNumber)) {
            contactsAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Emergency contact removed: " + phoneNumber, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Emergency contact not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeEmergencyContacts() {
        emergencyContactNumbers.clear();
        contactsAdapter.notifyDataSetChanged();
        Toast.makeText(this, "All emergency contacts removed", Toast.LENGTH_SHORT).show();
    }
}
