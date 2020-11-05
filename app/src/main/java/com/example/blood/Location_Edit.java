package com.example.blood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Location_Edit extends AppCompatActivity  implements OnMapReadyCallback {

    GoogleMap mMap;
    String lng,lat,context;
    FloatingActionButton Buttonsave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location__edit);

        SupportMapFragment supportMapFragment =(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map_location_edit);
        supportMapFragment.getMapAsync(this);
        lng = getIntent().getStringExtra("long");
        lat = getIntent().getStringExtra("lat");
        //context= getIntent().getStringExtra("context");
        Buttonsave = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        Buttonsave_clicked();
    }

    private void Buttonsave_clicked() {
   Buttonsave.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {

               Intent intent = new Intent(Location_Edit.this, donner_login.class);

               intent.putExtra("marker","0");
           intent.putExtra("action","");

               intent.putExtra("lng",lng);
               intent.putExtra("lat",lat);

               startActivity(intent);
               finish();



       }
   });

    }

    public void onMapReady(final GoogleMap googleMap) {

        mMap = googleMap;

        LatLng pos = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        mMap.addMarker(new MarkerOptions().position(pos).title("Donner position"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,10));



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                MarkerOptions markerOptions= new MarkerOptions();
                markerOptions.position(latLng);


                markerOptions.title(latLng.latitude+" : "+latLng.longitude);

                lng = String.valueOf(latLng.longitude);
                lat = String.valueOf(latLng.latitude);
                mMap.clear();
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                mMap.addMarker(markerOptions);

            }
        });

    }


}