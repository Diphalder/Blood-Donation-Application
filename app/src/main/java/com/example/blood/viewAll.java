package com.example.blood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class viewAll extends AppCompatActivity {

    public String cloudRef="DonnerInfo";

    String curMonth ,curYear,needBlood;
    ListView listView;
    DatabaseReference databaseReference;
    List<Donner> dataList;
    customAdaptor customAdaptor;
    AlertDialog.Builder alertdialog;
    ProgressBar progressBar;

    FusedLocationProviderClient fusedLocationProviderClient;
    String  latitude ,longitude , currentAddress ,countryName , locality;
    static  final  int REQUEST_CALL = 1;
    String PHONE;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        cast();
        getLocation();

    }

    private void cast() {

        Calendar calendar = Calendar.getInstance();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        curMonth = monthValue(monthFormat.format(calendar.getTime()));
        curYear = yearFormat.format(calendar.getTime());

        listView=(ListView)findViewById(R.id.listView);

        needBlood = getIntent().getStringExtra("blood");

        databaseReference = FirebaseDatabase.getInstance().getReference(cloudRef);
        dataList = new ArrayList<>();
        customAdaptor =  new customAdaptor(viewAll.this,dataList,needBlood, latitude ,longitude , currentAddress ,countryName , locality ,curMonth,curYear);

        progressBar =(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        listViewClicked();
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
    protected void onStart() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                dataList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    Donner data= dataSnapshot1.getValue(Donner.class);
                    dataList.add(data);
                }
                listView.setAdapter(customAdaptor) ;

                progressBar.setVisibility(View.GONE);
                Toast.makeText(viewAll.this,"Search complete",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(viewAll.this,"Empty",Toast.LENGTH_SHORT).show();
            }
        });

        super.onStart();
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
                        Geocoder geocoder = new Geocoder(viewAll.this, Locale.getDefault());
                        List<Address> addressList = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1
                        );

                        latitude =String.valueOf(addressList.get(0).getLatitude());
                        longitude =String.valueOf(addressList.get(0).getLongitude());
                        currentAddress =addressList.get(0).getAddressLine(0);
                        countryName = addressList.get(0).getCountryName();
                        locality =addressList.get(0).getLocality();
                        //Toast.makeText(viewAll.this,currentAddress,Toast.LENGTH_SHORT).show();



                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }
        });
    }


    private void listViewClicked() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final Donner data = dataList.get(position);

                PHONE = data.phone;

                alertdialog = new AlertDialog.Builder(viewAll.this);
                alertdialog.setTitle("Donner info");
                alertdialog.setMessage("name  :  "+data.name+"\nPhone  :  "+data.phone+"\nAge : "+data.age+"\ngender  :  "+data.gender+"\nWeight  :  "+data.weight+"(kg)\nBlood group  :  "+data.bloodgroup+"\nLast donet "+data.lastdonet+" month ago\nCurrent Location:\n"+data.currentAddress+"\n");
                alertdialog.setPositiveButton("call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        makePhoneCall(data.phone);

                    }
                });

                alertdialog.setNeutralButton("Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(viewAll.this,MapsActivity.class);
                        intent.putExtra("long",data.longitude);
                        intent.putExtra("lat",data.latitude);
                        startActivity(intent);

                    }
                });
                alertdialog.create().show();

            }
        });

    }



    private void makePhoneCall( String phone) {

        if(phone.trim().length()>0)
        {
            if(ContextCompat.checkSelfPermission(viewAll.this,Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(viewAll.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

            }
            else
            {
                String s = "tel:"+phone;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(s)));

            }

        }
        else
        {
            Toast.makeText(viewAll.this,"empty",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                makePhoneCall(PHONE);
            }
            else
            {
                Toast.makeText(viewAll.this,"permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }


}
