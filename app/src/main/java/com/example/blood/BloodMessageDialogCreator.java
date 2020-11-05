package com.example.blood;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.blood.R.*;

public class BloodMessageDialogCreator extends AppCompatDialogFragment {

    public String cloudRef = "RcvrInfo";
    Spinner spinnerbloodGrp;
    ImageButton ButtonEditLocation;
    EditText editTextPhone;
    String Date, Month ,Year;


    String latitude, longitude, currentAddress, countryName, locality, password;
    FusedLocationProviderClient fusedLocationProviderClient;



    String [] bloodgrp;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(layout.blood_message,null);
        builder.setView(view);


        builder.setTitle("Create a message for urgent blood");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //data store

                String phone = editTextPhone.getText().toString();
                String bloodgrp = spinnerbloodGrp.getSelectedItem().toString();
                if(phone.isEmpty())
                {
                    Toast.makeText(getContext(),"phone field is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                BloodMessage data = new BloodMessage( phone, bloodgrp, latitude, longitude, currentAddress ,Date,Month,Year);

                DatabaseReference ref = FirebaseDatabase.getInstance().getReference(cloudRef);
                String key = ref.push().getKey();
                ref.child(key).setValue(data);
                Toast.makeText(getContext(),"data uploaded successfully",Toast.LENGTH_SHORT).show();
            }
        });


        //casting

        editTextPhone = (EditText)view.findViewById(id.editTextPhone_dialogLayout);
        spinnerbloodGrp =(Spinner)view.findViewById(id.spinnerBloodGrp_dialogframe);
        bloodgrp=getResources().getStringArray(array.BloodGrp);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        getLocation();
        dateFind();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.sample_blood_grp,R.id.textViewSampleLayoutBloodgrp,bloodgrp);
        spinnerbloodGrp.setAdapter(adapter);

        return  builder.create();

    }

    private void dateFind() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd");

        Month = monthValue(monthFormat.format(calendar.getTime()));
        Year = yearFormat.format(calendar.getTime());
        Date = dateFormat.format(calendar.getTime());
        Toast.makeText(getContext(),"Date : "+Date+"/"+Month+"/"+Year,Toast.LENGTH_SHORT).show();

    }


    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );


                        latitude = String.valueOf(addressList.get(0).getLatitude());
                        longitude = String.valueOf(addressList.get(0).getLongitude());
                        currentAddress = addressList.get(0).getAddressLine(0);
                        countryName = addressList.get(0).getCountryName();
                        locality = addressList.get(0).getLocality();

                        Toast.makeText(getContext(),currentAddress,Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }
    String monthValue(String month)
    {

        if(month.equals("Jan"))
        {
            return "1";
        }
        if(month.equals("Feb"))
        {
            return "2";
        }
        if(month.equals("Mar"))
        {
            return "3";
        }
        if(month.equals("Apr"))
        {
            return "4";
        }
        if(month.equals("May"))
        {
            return "5";
        }
        if(month.equals("Jun"))
        {
            return "6";
        }
        if(month.equals("Jul"))
        {
            return "7";
        }
        if(month.equals("Aug"))
        {
            return "8";
        }
        if(month.equals("Sep"))
        {
            return "9";
        }
        if(month.equals("Oct"))
        {
            return "10";
        }
        if(month.equals("Nov"))
        {
            return "11";
        }
        if(month.equals("Dec"))
        {
            return "12";
        }
        else
        {
            return  null;
        }

    }





}
