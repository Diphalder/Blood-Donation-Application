package com.example.blood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class donner_login extends AppCompatActivity {
    public String cloudRef = "DonnerInfo";


    private Button buttonSave;
    private EditText editTextName, editTextPhone, editTextAge, editTextWeight;
    private Spinner spinnerBloodgrp, spinnerLastDonet;
    private RadioButton radioButtonMale, radioButtonFemale, radioButtonOthersGen;
    String[] lastblood, bloodgrp;
    AlertDialog.Builder alertdialog;
    String month, year;
    FusedLocationProviderClient fusedLocationProviderClient;

    ImageButton imageButtonEditLocation;

    TextView textViewLocation;

    FirebaseDatabase database;
    DatabaseReference myRef;
    String latitude, longitude, currentAddress, countryName, locality, password;
    String marker, ACTION;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donner_login);
        getSupportActionBar().setTitle("Blood Donner Info");

        marker = getIntent().getStringExtra("marker");
        ACTION = getIntent().getStringExtra("action");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Cast();
        if (ACTION.equals("edit")) {

            String name = getIntent().getStringExtra("name");
            String phone = getIntent().getStringExtra("phone");
            String age = getIntent().getStringExtra("age");
            String gender = getIntent().getStringExtra("gender");
            String weight = getIntent().getStringExtra("weight");
            String bloodgrp = getIntent().getStringExtra("bloodgrp");
            String lastdonet = getIntent().getStringExtra("lastdonet");

            editTextName.setText(name);
            editTextAge.setText(age);
            editTextPhone.setText(phone);
            editTextWeight.setText(weight);
            if (gender.equals("male")) {
                radioButtonMale.setChecked(true);

            } else if (gender.equals("female")) {
                radioButtonFemale.setChecked(true);
            } else {
                radioButtonOthersGen.setChecked(true);
            }
            int index = 0;
            String [] list =getResources().getStringArray(R.array.BloodGrp);
            for (int i = 0; i < list.length; i++) {
                if (list[i].equals(bloodgrp)) {
                    index = i;
                    break;
                }
            }
            spinnerBloodgrp.setSelection(index);

            String [] list2 = getResources().getStringArray(R.array.LastDonet);
            for (int i = 0; i < list2.length; i++) {
                if (list2[i].equals(lastdonet)) {
                    index = i;
                    break;
                }
            }
            spinnerLastDonet.setSelection(index);
            imageButtonEditLocation.setVisibility(View.GONE);
            Toast.makeText(getApplication(),"Previous data loaded",Toast.LENGTH_SHORT).show();
        }


        buttonSaveClicked();


        if (marker.equals("1")) {
            getLocation();
        } else {
            editedLocation();

        }
        EditLocation_Clicked();


    }

    private void editedLocation() {


        String lng = getIntent().getStringExtra("lng");
        String lat = getIntent().getStringExtra("lat");
        //   Toast.makeText(donner_login.this,lng+" "+lat,Toast.LENGTH_SHORT).show();


        Geocoder geocoder = new Geocoder(donner_login.this, Locale.getDefault());
        try {
            List<Address> addressList = geocoder.getFromLocation(Double.valueOf(lat), Double.valueOf(lng), 1);

            latitude = String.valueOf(addressList.get(0).getLatitude());
            longitude = String.valueOf(addressList.get(0).getLongitude());
            currentAddress = addressList.get(0).getAddressLine(0);
            countryName = addressList.get(0).getCountryName();
            locality = addressList.get(0).getLocality();
            //Toast.makeText(donner_login.this,currentAddress,Toast.LENGTH_SHORT).show();
            textViewLocation.setText(currentAddress);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void EditLocation_Clicked() {
        imageButtonEditLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(donner_login.this, Location_Edit.class);
                intent.putExtra("long", longitude);
                intent.putExtra("lat", latitude);
                //intent.putExtra("context","donner_login");
                finish();
                startActivity(intent);

            }
        });
    }

    private void buttonSaveClicked() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = editTextName.getText().toString();
                final String age = editTextAge.getText().toString();
                final String phone = editTextPhone.getText().toString();
                final String weight = editTextWeight.getText().toString();
                final String bloodgrp = spinnerBloodgrp.getSelectedItem().toString();
                final String lastdonet = spinnerLastDonet.getSelectedItem().toString();
                final String gender;
                //Toast.makeText(donner_login.this,"run",Toast.LENGTH_LONG).show();

                if (name.isEmpty() == true) {
                    editTextName.setError("enter Name");
                    editTextName.requestFocus();
                    return;
                }
                if (phone.isEmpty() == true) {
                    editTextPhone.setError("enter Phone number");
                    editTextPhone.requestFocus();
                    return;
                }
                if (age.isEmpty() == true) {
                    editTextAge.setError("enter Age");
                    editTextAge.requestFocus();
                    return;
                }
                if (radioButtonFemale.isChecked() == true) {
                    gender = "female";
                } else if (radioButtonMale.isChecked() == true) {
                    gender = "male";
                } else if (radioButtonOthersGen.isChecked() == true) {
                    gender = "others";
                } else {
                    Toast.makeText(donner_login.this, "select gender", Toast.LENGTH_SHORT).show();
                    return;

                }


                if (weight.isEmpty() == true) {
                    editTextWeight.setError("enter your body weight");
                    editTextWeight.requestFocus();
                    return;
                }


                alertdialog = new AlertDialog.Builder(donner_login.this);
                alertdialog.setTitle("Donner info");
                alertdialog.setMessage("name  :  " + name + "\nPhone  :  " + phone + "\nAge : " + age + "\ngender  :  " + gender + "\nWeight  :  " + weight + "(kg)\nBlood group  :  " + bloodgrp + "\nLast donet " + lastdonet + " month ago\nCurrent Location:\n" + currentAddress + "\n");

                final EditText editPASSWORD = new EditText(donner_login.this);
                editPASSWORD.setInputType(InputType.TYPE_CLASS_TEXT);
                editPASSWORD.setHint("  enter your password");

                alertdialog.setView(editPASSWORD);


                alertdialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        password = editPASSWORD.getText().toString();
                        if (password.isEmpty() == true) {
                            Toast.makeText(donner_login.this, "enter password", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (password.length() < 6) {
                            Toast.makeText(donner_login.this, "password is less than 6 letter", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        Donner donner = new Donner(name, age, phone, weight, bloodgrp, lastdonet, gender, month, year, latitude, longitude, currentAddress, locality, password);

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(cloudRef).child(phone);
                        ref.setValue(donner);

                        Toast.makeText(donner_login.this, "data uploaded successfully", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(donner_login.this, Donner_profile.class);
                        finish();
                        startActivity(intent);


                    }
                });
                alertdialog.setNeutralButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                alertdialog.create().show();


            }
        });
    }

    private void Cast() {
        buttonSave = (Button) findViewById(R.id.buttonSave);
        spinnerBloodgrp = (Spinner) findViewById(R.id.spinnerBloodgrp);
        spinnerLastDonet = (Spinner) findViewById(R.id.spinnerLastDonet);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioButtonFemale);
        radioButtonOthersGen = (RadioButton) findViewById(R.id.radioButtonOthers);
        radioButtonMale = (RadioButton) findViewById(R.id.radioButtonMale);

        textViewLocation = (TextView) findViewById(R.id.textViewUserLocation);

        editTextAge = (EditText) findViewById(R.id.editTextAge);
        editTextPhone = (EditText) findViewById(R.id.editTextPhn);
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextWeight = (EditText) findViewById(R.id.editTextWeight);
        imageButtonEditLocation = (ImageButton) findViewById(R.id.imageButtonEdit);

        bloodgrp = getResources().getStringArray(R.array.BloodGrp);
        lastblood = getResources().getStringArray(R.array.LastDonet);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.sample_blood_grp, R.id.textViewSampleLayoutBloodgrp, bloodgrp);
        spinnerBloodgrp.setAdapter(adapter);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, R.layout.samplelayout_lastblood, R.id.textView444, lastblood);
        spinnerLastDonet.setAdapter(adapter2);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        month = monthValue(monthFormat.format(calendar.getTime()));
        year = yearFormat.format(calendar.getTime());


    }

    String monthValue(String month) {

        if (month.equals("Jan")) {
            return "1";
        }
        if (month.equals("Feb")) {
            return "2";
        }
        if (month.equals("Mar")) {
            return "3";
        }
        if (month.equals("Apr")) {
            return "4";
        }
        if (month.equals("May")) {
            return "5";
        }
        if (month.equals("Jun")) {
            return "6";
        }
        if (month.equals("Jul")) {
            return "7";
        }
        if (month.equals("Aug")) {
            return "8";
        }
        if (month.equals("Sep")) {
            return "9";
        }
        if (month.equals("Oct")) {
            return "10";
        }
        if (month.equals("Nov")) {
            return "11";
        }
        if (month.equals("Dec")) {
            return "12";
        } else {
            return null;
        }

    }


    private void getLocation() {

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
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {


                    try {
                        Geocoder geocoder = new Geocoder(donner_login.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );


                        latitude = String.valueOf(addressList.get(0).getLatitude());
                        longitude = String.valueOf(addressList.get(0).getLongitude());
                        currentAddress = addressList.get(0).getAddressLine(0);
                        countryName = addressList.get(0).getCountryName();
                        locality = addressList.get(0).getLocality();
                        //Toast.makeText(donner_login.this,currentAddress,Toast.LENGTH_SHORT).show();
                        textViewLocation.setText(currentAddress);


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }


}
