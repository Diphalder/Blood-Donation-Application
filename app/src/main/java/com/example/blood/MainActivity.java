package com.example.blood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements sample_dialog.ExampleDialogListener {
    public String cloudRef = "DonnerInfo";
    Button buttonDoner, buttonDonnerSignIN, buttonAddMessageForBlood;
   Button imageViewUrgent_call;
    String PHONE, PASS;
    DatabaseReference databaseReference;

    dataBaseHelper mydb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonDoner = (Button) findViewById(R.id.buttonDoner);
        imageViewUrgent_call = (Button) findViewById(R.id.button_urgent_call);
        buttonDonnerSignIN = (Button) findViewById(R.id.buttonDonnerSignIn);
        databaseReference = FirebaseDatabase.getInstance().getReference(cloudRef);
        buttonAddMessageForBlood = (Button) findViewById(R.id.buttonAddmessageForBlood);
        mydb = new dataBaseHelper(this);


        run();
        buttonDonerClicked();
        imageViewUrgent_call_Clicked();
        buttonDonnerSignIN_Clicked();
        buttonAddMessageForBlood_clicked();


        autoLoginAc();



    }

    private void buttonAddMessageForBlood_clicked() {


        buttonAddMessageForBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BloodMessageDialogCreator sample_dialog = new BloodMessageDialogCreator();
                sample_dialog.show(getSupportFragmentManager(), "exmple dialog");

            }

        });

    }

    private void buttonDonnerSignIN_Clicked() {
        buttonDonnerSignIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sample_dialog sample_dialog = new sample_dialog();
                sample_dialog.show(getSupportFragmentManager(), "exmple dialog");

            }
        });

    }

    private void imageViewUrgent_call_Clicked() {
        imageViewUrgent_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Urgent_Blood_Dialog dialog = new Urgent_Blood_Dialog();
                dialog.show(getSupportFragmentManager(), "exmple dialog");

            }
        });


    }

    private void buttonDonerClicked() {
        buttonDoner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, donner_login.class);
                intent.putExtra("marker", "1");
                intent.putExtra("action","");

                startActivity(intent);


            }
        });
    }


    private void run() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }
    }

    public void login() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                boolean find = false;
                boolean phoneFind = false;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Donner data = dataSnapshot1.getValue(Donner.class);
                    if (PHONE.equals(data.phone) == true && PASS.equals(data.password) == true) {
                        find = true;
                        break;
                    }
                    if (PHONE.equals(data.phone) == true) {
                        phoneFind = true;
                    }
                }
                if (find) {
                    Toast.makeText(MainActivity.this, "Login successfully", Toast.LENGTH_SHORT).show();


                    boolean result = mydb.insertData(PHONE,PASS);

                    if (result == true)
                    {

                        Intent intent = new Intent(MainActivity.this, Donner_profile.class);
                        finish();
                        startActivity(intent);

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Insert Failed",Toast.LENGTH_SHORT).show();

                    }




                } else {
                    if (phoneFind) {
                        Toast.makeText(MainActivity.this, "wrong pass", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "wrong phone number", Toast.LENGTH_SHORT).show();
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, "wrong phone number", Toast.LENGTH_SHORT).show();

            }
        });


    }


    public void applyTests(String phone, String pass) {

        PASS = pass;
        PHONE = phone;
        login();

    }


    private void autoLoginAc()
    {
        Cursor ResData = mydb.getAllData();
        if (ResData.getCount() != 0)
        {
            Intent intent = new Intent(MainActivity.this, Donner_profile.class);
            finish();
            startActivity(intent);
        }

    }


}
