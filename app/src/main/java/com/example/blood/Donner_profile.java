package com.example.blood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Donner_profile extends AppCompatActivity {

    dataBaseHelper mydb;
    DatabaseReference databaseReference;
    public String cloudRef = "DonnerInfo";
    String PHONE, PASS;

    Donner User; //data from cloud

    FusedLocationProviderClient fusedLocationProviderClient;

    String  latitude ,longitude , currentAddress ,countryName , locality;  //current location

    Button button_ViewMessage,btAddMessage ,btUrgnetCall;
    TextView textView_ProfileInfo ,textView_userLocation;
    ProgressBar progressBar;
    ImageButton imageButtonSync,imageButtonProfileEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donner_profile);
        getSupportActionBar().setTitle("Blood Donner Profile");
        casting();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.Item_logout) {

            mydb.deleteAll();
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);

        }
        if (item.getItemId() == R.id.item_EditProfile) {


        }
        return super.onOptionsItemSelected(item);
    }

    private void casting() {

        mydb = new dataBaseHelper(this);
        databaseReference = FirebaseDatabase.getInstance().getReference(cloudRef);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        button_ViewMessage=(Button)findViewById(R.id.buttonViewMEssage);
        textView_ProfileInfo=(TextView)findViewById(R.id.textViewUserInfo);
        textView_userLocation=(TextView)findViewById(R.id.textViewUserLcation);
        btAddMessage=(Button)findViewById(R.id.button200);
        btUrgnetCall=(Button)findViewById(R.id.button300);
        progressBar=(ProgressBar)findViewById(R.id.progressBar222);

        imageButtonSync=(ImageButton)findViewById(R.id.imageButtonSync);

        imageButtonProfileEdit=(ImageButton)findViewById(R.id.imageButton22222Edit);
        getDataFromRoomDataBase();

        button_ViewMessage_clicked();
        btUrgnetCall_clicked();
        btAddMessage_clicked();
        imageButtonSync_clicked();
        imageButtonProfileEdit_clicked();

    }

    private void imageButtonProfileEdit_clicked() {
    imageButtonProfileEdit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Donner_profile.this,donner_login.class);
            intent.putExtra("action","edit");
            intent.putExtra("marker", "1");



            intent.putExtra("name", User.name);
            intent.putExtra("phone",User.phone );
            intent.putExtra("age", User.age);
            intent.putExtra("weight", User.weight);
            intent.putExtra("bloodgrp", User.bloodgroup);
            intent.putExtra("gender", User.gender);
            intent.putExtra("lastdonet", User.lastdonet);





            startActivity(intent);

        }
    });

    }

    private void imageButtonSync_clicked() {

    imageButtonSync.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
            getDataFromRoomDataBase();
            //progressBar.setVisibility(View.GONE);
        }
    });

    }

    private void btAddMessage_clicked() {


    btAddMessage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            BloodMessageDialogCreator sample_dialog = new BloodMessageDialogCreator();
            sample_dialog.show(getSupportFragmentManager(), "exmple dialog");

        }
    });

    }

    private void btUrgnetCall_clicked() {
   btUrgnetCall.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

           Urgent_Blood_Dialog dialog = new Urgent_Blood_Dialog();
           dialog.show(getSupportFragmentManager(), "exmple dialog");

       }
   });

    }

    private void button_ViewMessage_clicked() {
    button_ViewMessage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Donner_profile.this,MessageViwer.class);
            intent.putExtra("blood",User.bloodgroup);
            startActivity(intent);

        }
    });

    }

    private void profile_Edit() {

        textView_ProfileInfo.setText("Name : "+User.name+"\nPhone : "+User.phone+"\nBloodGroup : "+User.bloodgroup);
        textView_userLocation.setText("\nCurrent location:\n"+User.currentAddress);
        progressBar.setVisibility(View.GONE);

    }

    private void updateLocation() {

        User.setLatitude(latitude);
        User.setLongitude(longitude);
        User.setCurrentAddress(currentAddress);
        User.setLocality(locality);


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(cloudRef).child(PHONE);
        ref.setValue(User);
        profile_Edit();

    }

    private void getDataFromRoomDataBase() {

        progressBar.setVisibility(View.VISIBLE);
        Cursor ResData =  mydb.getAllData();
        if(ResData.getCount()==0)
        {
            loginFailed();
            return;
        }
        else
        {
            while (ResData.moveToNext())
            {
                PHONE = ResData.getString(0);
                PASS = ResData.getString(1);
                autoLogin();
                break;
            }
        }


    }

    private void loginFailed()
    {
        mydb.deleteAll();
        Intent intent = new Intent(Donner_profile.this,MainActivity.class);
        finish();
        startActivity(intent);

    }

    public void autoLogin() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean find = false;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    User = dataSnapshot1.getValue(Donner.class);
                    if (PHONE.equals(User.phone) == true && PASS.equals(User.password) == true) {
                        find = true;
                        break;
                    }
                }

                if (find)
                {
                    Toast.makeText(Donner_profile.this, "Login successfully", Toast.LENGTH_SHORT).show();
                    getLocation();
                }
                else
                    {
                        Toast.makeText(Donner_profile.this, "Error", Toast.LENGTH_SHORT).show();
                        loginFailed();
                    }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Donner_profile.this, "Error", Toast.LENGTH_SHORT).show();
                loginFailed();

            }
        });

    }
    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions

            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {


                    try {
                        Geocoder geocoder = new Geocoder(Donner_profile.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );


                        latitude =String.valueOf(addressList.get(0).getLatitude());
                        longitude =String.valueOf(addressList.get(0).getLongitude());
                        currentAddress =addressList.get(0).getAddressLine(0);
                        countryName = addressList.get(0).getCountryName();
                        locality =addressList.get(0).getLocality();
                       // Toast.makeText(Donner_profile.this,currentAddress,Toast.LENGTH_SHORT).show();

                        updateLocation();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }


}