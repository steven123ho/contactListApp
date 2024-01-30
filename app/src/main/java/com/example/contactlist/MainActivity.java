package com.example.contactlist;

import androidx.appcompat.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import androidx.fragment.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    //in the book this is separated to currentContact = new Contact();
    private Contact currentContact = new Contact();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListButton();
        initToggleButton();
        setForEditing(false);
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();
    }


    //Navigation to maps and to settings
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
        } else {
            ScrollView s = findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    @Override
    public void didFinishDatePickerDialog(Calendar selectedTime) {
        TextView dateLabel = findViewById(R.id.dateLabel);
        dateLabel.setText(DateFormat.format("MM/dd/yyyy", selectedTime));
        currentContact.setBirthday(selectedTime);
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

    private void initTextChangedEvents() {

        final EditText etContactName = findViewById(R.id.nameInput);
        etContactName.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setContactName(etContactName.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        final EditText etStreetAddress = findViewById(R.id.addressInput);
        etStreetAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setStreetAddress(etStreetAddress.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        final EditText etCity = findViewById(R.id.cityInput);
        etCity.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCity(etCity.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        final EditText etState = findViewById(R.id.stateInput);
        etState.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setState(etState.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        final EditText etZipCode = findViewById(R.id.zipInput);
        etZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setZipCode(etZipCode.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        final EditText etHomePhone = findViewById(R.id.homeInput);
        etHomePhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setPhoneNumber(etHomePhone.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        final EditText etCellPhone = findViewById(R.id.cellInput);
        etCellPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setCellNumber(etCellPhone.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        final EditText etEmail = findViewById(R.id.emailInput);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                currentContact.setEmail(etEmail.getText().toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // autogenerated method for Text Watcher
            }
        });

        etHomePhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etCellPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }


    private void initSaveButton () {

        Button saveButton = findViewById(R.id.saveBtn);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean wasSuccessful;
                hideKeyboard();
                ContactDataSource ds = new ContactDataSource(MainActivity.this);
                try {

                    ds.open();

                    if(currentContact.getContactID() == -1) {
                        wasSuccessful = ds.insertContact(currentContact);
                        if (wasSuccessful) {
                            int newId = ds.getLastContactID();
                            currentContact.setContactID(newId);
                        }
                    } else {
                        wasSuccessful = ds.updateContact(currentContact);
                    }
                    ds.close();
                } catch (Exception e) {
                    wasSuccessful = false;
                }

                if (wasSuccessful) {
                    ToggleButton editToggle = findViewById(R.id.toggleBtnEdit);
                    editToggle.toggle();
                    setForEditing(false);
                }
            }
        });


    }

    private void hideKeyboard () {
        InputMethodManager imm = (InputMethodManager) getSystemService(MainActivity.this.INPUT_METHOD_SERVICE);

        EditText nameInput = findViewById(R.id.nameInput);
        imm.hideSoftInputFromWindow(nameInput.getWindowToken(), 0);

        EditText addressInput = findViewById(R.id.addressInput);
        imm.hideSoftInputFromWindow(addressInput.getWindowToken(), 0);

        EditText stateInput = findViewById(R.id.stateInput);
        imm.hideSoftInputFromWindow(stateInput.getWindowToken(), 0);

        EditText cityInput = findViewById(R.id.cityInput);
        imm.hideSoftInputFromWindow(cityInput.getWindowToken(), 0);

        EditText zipCodeInput = findViewById(R.id.zipInput);
        imm.hideSoftInputFromWindow(zipCodeInput.getWindowToken(), 0);

        EditText homeInput = findViewById(R.id.homeInput);
        imm.hideSoftInputFromWindow(homeInput.getWindowToken(), 0);

        EditText cellInput = findViewById(R.id.cellInput);
        imm.hideSoftInputFromWindow(cellInput.getWindowToken(), 0);

        EditText emailInput = findViewById(R.id.emailInput);
        imm.hideSoftInputFromWindow(emailInput.getWindowToken(), 0);
    }





}




















