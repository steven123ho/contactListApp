package com.example.contactlist2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.concurrent.Callable;


public class MainActivity extends AppCompatActivity implements DatePickerDialog.SaveDateListener {

    //in the book this is separated to currentContact = new Contact();
    private Contact currentContact;

    final int PERMISSION_REQUEST_PHONE = 102;
    final int PERMISSION_REQUEST_CAMERA = 103;
    final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initListButton();
        initToggleButton();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            initContact(extras.getInt("contactID"));
        } else {
            currentContact = new Contact();
        }

        setForEditing(false);
        initChangeDateButton();
        initTextChangedEvents();
        initSaveButton();
        initCallFunction();
        initImageButton();
    }


    //Navigation to maps and to settings
    private void initListButton() {
        ImageButton mapButton = findViewById(R.id.mapBtn);
        ImageButton settingsButton = findViewById(R.id.settingsBtn);
        ImageButton listButton = findViewById(R.id.contactBtn);

        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                if (currentContact.getContactID() == 1) {
                    Toast.makeText(getBaseContext(), "Contact must be saved before it can be mapped", Toast.LENGTH_LONG).show();
                } else {
                    intent.putExtra("contactid", currentContact.getContactID());
                }
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

        listButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, activity_contact_list.class);
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
        ImageButton picture = findViewById(R.id.imageContact);

        nameInput.setEnabled(enabled);
        addressInput.setEnabled(enabled);
        cityInput.setEnabled(enabled);
        stateInput.setEnabled(enabled);
        zipInput.setEnabled(enabled);
        emailInput.setEnabled(enabled);
        changeBtn.setEnabled(enabled);
        saveBtn.setEnabled(enabled);
        picture.setEnabled(enabled);

        if (enabled) {
            nameInput.requestFocus();
            homeInput.setInputType(InputType.TYPE_CLASS_PHONE);
            cellInput.setInputType(InputType.TYPE_CLASS_PHONE);

        } else {
            ScrollView s = findViewById(R.id.scrollView);
            s.fullScroll(ScrollView.FOCUS_UP);
            homeInput.setInputType(InputType.TYPE_NULL);
            cellInput.setInputType(InputType.TYPE_NULL);
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

    //Gets specific contact from ContactDataSource and uses it to populate the fields
    private void initContact (int id) {

       ContactDataSource ds = new ContactDataSource(MainActivity.this);
       try {
           ds.open();
           currentContact = ds.getSpecificContact(id);
           ds.close();
       } catch (Exception e) {
           Toast.makeText(this,"Load Contact Failed", Toast.LENGTH_LONG).show();
       }

       EditText editName = findViewById(R.id.nameInput);
       EditText editAddress = findViewById(R.id.addressInput);
       EditText editCity = findViewById(R.id.cityInput);
       EditText editState = findViewById(R.id.stateInput);
       EditText editZipCode = findViewById(R.id.zipInput);
       EditText editPhone = findViewById(R.id.homeInput);
       EditText editCell = findViewById(R.id.cellInput);
       EditText editEmail = findViewById(R.id.emailInput);
       TextView birthday = findViewById(R.id.dateLabel);

       editName.setText(currentContact.getContactName());
       editAddress.setText(currentContact.getStreetAddress());
       editCity.setText(currentContact.getCity());
       editState.setText(currentContact.getState());
       editZipCode.setText(currentContact.getZipCode());
       editPhone.setText(currentContact.getPhoneNumber());
       editCell.setText(currentContact.getCellNumber());
       editEmail.setText(currentContact.getEmail());
       birthday.setText(DateFormat.format("MM/dd/yyyy", currentContact.getBirthday().getTimeInMillis()).toString());

    }


    //call function on long press of a contact number
    private void initCallFunction() {

        EditText editPhone = (EditText) findViewById(R.id.homeInput);
        editPhone.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                checkPhonePermission(currentContact.getPhoneNumber());
                return false;
            }
        });

        EditText editCell = (EditText) findViewById(R.id.cellInput);
        editCell.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                checkPhonePermission(currentContact.getPhoneNumber());
                return false;
            }
        });
    }


    // Ask user for permissions
    private void checkPhonePermission (String phoneNumber) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission (MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CALL_PHONE)) {
                    Snackbar.make(findViewById(R.id.activity_main), "MyContactList requires this permission to place a call from the app", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_PHONE);
                        }
                    }).show();
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_PHONE);
                }
            } else {
                callContact(phoneNumber);
            }
        } else {
            callContact(phoneNumber);
        }
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String permissions[],@NonNull int[] grantResults) {
        // added this super call because the IDE bug said so. not the book
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "You will now call from this app", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "You will not be able to call from this app", Toast.LENGTH_LONG).show();

                }
            }

            // permission for camera
            case PERMISSION_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(MainActivity.this, "You will not be able to save contact pictures from this app", Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    private void callContact (String phoneNumber) {
        Intent intent = new Intent (Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return ;
        } else {
            startActivity(intent);
        }
    }

    //Image Button for Contacts
    private void initImageButton () {
        ImageButton ib = findViewById(R.id.imageContact);
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.CAMERA)) {
                            Snackbar.make(findViewById(R.id.activity_main), "The app needs permission to take pictures", Snackbar.LENGTH_INDEFINITE).setAction("Ok", new View.OnClickListener(){
                               @Override
                               public void onClick (View view) {
                                   ActivityCompat.requestPermissions(MainActivity.this, new String[]{ Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                               }
                            }).show();
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, PERMISSION_REQUEST_CAMERA);
                        }

                    }else {
                        takePhoto();
                    }
                } else {
                    takePhoto();
                }
            }
        });
    }

    //take photo function
    public void takePhoto() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST) {
            if (resultCode == RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                Bitmap scaledPhoto = Bitmap.createScaledBitmap(photo, 144, 144, true);
                ImageButton imageContact = (ImageButton) findViewById(R.id.imageContact);
                imageContact.setImageBitmap(scaledPhoto);
                currentContact.setPicture(scaledPhoto);
            }
        }
    }


}
