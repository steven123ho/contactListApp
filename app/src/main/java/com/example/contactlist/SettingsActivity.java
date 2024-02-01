package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initListButton();
        initSettingsButton();
    }



    //Navigaion to maps and to settings
    private void initListButton() {
        ImageButton contactButton = findViewById(R.id.contactBtn);
        ImageButton mapButton = findViewById(R.id.mapBtn);
        contactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

    }

    private void initSettingsButton() {
        ImageButton settingsBtn = findViewById(R.id.settingsBtn);
        settingsBtn.setEnabled(false);
    }


    private void initSettings() {

        String sortBy = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortfield","contactname");
        String sortOrder = getSharedPreferences("MyContactListPreferences",
                Context.MODE_PRIVATE).getString("sortorder","ASC");

        RadioButton nameRadio = findViewById(R.id.nameRadio);
        RadioButton cityRadio = findViewById(R.id.cityRadio);
        RadioButton birthdayRadio = findViewById(R.id.birthdayRadio);

        if (sortBy.equalsIgnoreCase("contactname")) {
            nameRadio.setChecked(true);
        } else if (sortBy.equalsIgnoreCase("city")) {
            cityRadio.setChecked(true);
        } else {
            birthdayRadio.setChecked(true);
        }

        RadioButton ascendingRadio = findViewById(R.id.ascendingRadio);
        RadioButton descendingRadio =findViewById(R.id.descendingRadio);

        if (sortOrder.equalsIgnoreCase("ASC")) {
            ascendingRadio.setChecked(true);
        } else {
            descendingRadio.setChecked(true);
        }
    }


    private void initSortByClick() {

        RadioGroup rgSortBy = findViewById(R.id.sortByRadioGroup);
        rgSortBy.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton nameRadio = findViewById(R.id.nameRadio);
                RadioButton cityRadio = findViewById(R.id.cityRadio);
                if (nameRadio.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "contactname").apply();
                } else if (cityRadio.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield","city").apply();
                } else {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortfield", "birthday").apply();
                }
            }
        });
    }


    private void initSortOrderClick() {

        RadioGroup rgSortOrder = findViewById(R.id.orderByRadioGroup);
        rgSortOrder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton ascendingRadio = findViewById(R.id.ascendingRadio);
                if (ascendingRadio.isChecked()) {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "ASC").apply();
                } else {
                    getSharedPreferences("MyContactListPreferences",
                            Context.MODE_PRIVATE).edit()
                            .putString("sortorder", "DESC").apply();
                }
            }
        });
    }

}