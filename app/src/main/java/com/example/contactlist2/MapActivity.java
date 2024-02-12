package com.example.contactlist2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    final int PERMISSION_REQUEST_LOCATION = 101;
    GoogleMap gMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    ArrayList<Contact> contacts = new ArrayList<>();
    Contact currentContact = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Bundle extras = getIntent().getExtras();
        try {
            ContactDataSource ds = new ContactDataSource(MapActivity.this);
            ds.open();
            if(extras != null) {
                currentContact = ds.getSpecificContact(extras.getInt("contactid"));
            } else {
                contacts = ds.getContacts("contactname", "ASC");
            }
            ds.close();
        } catch (Exception e) {
            Toast.makeText(this, "Contact(s) could not be retrieved", Toast.LENGTH_LONG).show();
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //added a certain dependency not mentioned in book to make this line work
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        createLocationRequest();
        createLocationCallback();

        initMapTypeButtons();

        initListButton();
        initMapsButton();

    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            stopLocationUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void createLocationRequest () {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult (LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for  (Location location : locationResult.getLocations()) {
                    Toast.makeText(getBaseContext(), "Lat: " + location.getLatitude() + " Long: " + location.getLongitude() +
                            " Accuracy: " + location.getAccuracy(), Toast.LENGTH_LONG).show();
                }
            };
        };
    }

    private void startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED)    {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        gMap.setMyLocationEnabled(true);
    }

    private void stopLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)    {
            return;
        }
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onMapReady (GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        RadioButton rbNormal = findViewById(R.id.radioButtonNormal);
        rbNormal.setChecked(true);

        // Creates a marker on the map
        Point size = new Point();
        WindowManager w = getWindowManager();
        w.getDefaultDisplay().getSize(size);
        int measuredWidth = size.x;
        int measuredHeight = size.y;

        if (contacts.size() > 0) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for(int i = 0; i < contacts.size(); i++) {
                currentContact = contacts.get(i);

                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " +
                        currentContact.getState() + ", " +
                        currentContact.getZipCode();

                try {
                    addresses = geo.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                builder.include(point);

                gMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
            }
            gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), measuredWidth, measuredHeight, 450));

        } else {
            if (currentContact != null) {
                Geocoder geo = new Geocoder(this);
                List<Address> addresses = null;

                String address = currentContact.getStreetAddress() + ", " +
                        currentContact.getCity() + ", " +
                        currentContact.getState() + ", " +
                        currentContact.getZipCode();

                try {
                    addresses = geo.getFromLocationName(address, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LatLng point = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());

                gMap.addMarker(new MarkerOptions().position(point).title(currentContact.getContactName()).snippet(address));
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 16));
            } else {
                AlertDialog alertDialog = new AlertDialog.Builder (MapActivity.this).create();
                alertDialog.setTitle("No Data");
                alertDialog.setMessage("No data is available for the mapping function");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                alertDialog.show();
            }
        }


        //Request Device for location accessibility
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                if (ContextCompat.checkSelfPermission(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        Snackbar.make(findViewById(R.id.activity_contact_map), "MyContactList requires this permission to locate " + "your contacts", Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(MapActivity.this, new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_LOCATION);
                            }
                        }).show();
                    } else {
                        ActivityCompat.requestPermissions(MapActivity.this, new
                                        String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                PERMISSION_REQUEST_LOCATION);
                    }
                } else {
                    startLocationUpdates();
                }
            } else {
                startLocationUpdates();
            }
        } catch (Exception e){
            Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();
        }
    }


    private void initMapTypeButtons() {
        RadioGroup rgMapType = findViewById(R.id.radioGroupMapType);
        rgMapType.setOnCheckedChangeListener (new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged (RadioGroup radioGroup, int i) {
                RadioButton rbNormal = findViewById(R.id.radioButtonNormal);
                if (rbNormal.isChecked()) {
                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else {
                    gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
            }
        });
    }


/*
    private void  startLocationUpdates() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getBaseContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        try {
            locationManager = (LocationManager) getBaseContext().getSystemService(Context.LOCATION_SERVICE);
            gpsListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.latitudeResult);
                    TextView txtLongitude = (TextView) findViewById(R.id.longitudeResult);
                    TextView txtAccuracy = (TextView) findViewById(R.id.accuracyResult);
                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));

                    if (isBetterLocation(location)) {
                        currentBestLocation = location;
                        //display in location in textViews
                    } //no else block; if not better just ignores
                }

                public void onStatusChanged (String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
            };

            networkListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    TextView txtLatitude = (TextView) findViewById(R.id.latitudeResult);
                    TextView txtLongitude = (TextView) findViewById(R.id.longitudeResult);
                    TextView txtAccuracy = (TextView) findViewById(R.id.accuracyResult);
                    txtLatitude.setText(String.valueOf(location.getLatitude()));
                    txtLongitude.setText(String.valueOf(location.getLongitude()));
                    txtAccuracy.setText(String.valueOf(location.getAccuracy()));


                    if (isBetterLocation(location)) {
                        currentBestLocation = location;
                        //display in location in textViews
                    } //no else block; if not better just ignores
                }

                public void onStatusChanged (String provider, int status, Bundle extras) {}
                public void onProviderEnabled(String provider) {}
                public void onProviderDisabled(String provider) {}
            };

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, gpsListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, networkListener);

        } catch (Exception e){
            Toast.makeText(getBaseContext(), "Error, Location not available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions [], int[] grantResults) {
        //super call was required by IDE but not in book
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(MapActivity.this, "MyContactList will not locate your contacts.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
*/


}