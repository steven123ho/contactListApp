package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListButton();
        initToggleButton();
        setForEditing(false);
        initChangeDateButton();
    }


    //Navigaion to maps and to settings
    private void initListButton() {
        ImageButton mapButton = findViewById(R.id.mapBtn);
        ImageButton settingsButton = findViewById(R.id.settingsBtn);
        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //clears the stack trace
                startActivity(intent);
            }
        });

    }

    //toggle button and set for editing enabler
    private void initToggleButton() {
        final ToggleButton editToggle = findViewById(R.id.toggleBtnEdit);
        editToggle.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                setForEditing(editToggle.isChecked());
            }

        });
    }

    private void setForEditing(boolean enabled) {
        EditText nameInput = findViewById(R.id.nameInput);
        EditText addressInput = findViewById(R.id.addressInput);
        EditText cityInput = findViewById(R.id.cityInput);
        EditText stateInput = findViewById(R.id.stateInput);
        EditText zipInput = findViewById(R.id.zipInput);
        EditText homeInput = findViewById(R.id.homeInput);
        EditText cellInput = findViewById(R.id.cellInput);
        EditText emailInput = findViewById(R.id.emailInput);
        Button changeBtn = findViewById(R.id.birthdayBtn);
        Button saveBtn = findViewById(R.id.saveBtn);

        nameInput.setEnabled(enabled);
        addressInput.setEnabled(enabled);
        cityInput.setEnabled(enabled);
        stateInput.setEnabled(enabled);
        zipInput.setEnabled(enabled);
        homeInput.setEnabled(enabled);
        cellInput.setEnabled(enabled);
        emailInput.setEnabled(enabled);
        changeBtn.setEnabled(enabled);
        saveBtn.setEnabled(enabled);

        if (enabled) {
            nameInput.requestFocus();
        }
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        TextView dateLabel = findViewById(R.id.dateLabel);
        dateLabel.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
    }

    //Change Birthday Button
    private void initChangeDateButton() {
        Button changeDate = findViewById(R.id.birthdayBtn);
        changeDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                DatePickerDialog datePickerDialog = new DatePickerDialog();
                datePickerDialog.show(fm, "DatePick");
            }
        });
    }
}

