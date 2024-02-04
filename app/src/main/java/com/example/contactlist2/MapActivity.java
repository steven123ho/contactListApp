package com.example.contactlist2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MapActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initListButton();
        initMapsButton();
    }

    //Navigaion to maps and to settings
    private void initListButton() {
        ImageButton contactButton = findViewById(R.id.contactBtn);
        ImageButton settingsButton = findViewById(R.id.settingsBtn);
        contactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, activity_contact_list.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

    }

    private void initMapsButton() {
        ImageButton mapBtn = findViewById(R.id.mapBtn);
        mapBtn.setEnabled(false);
    }

}