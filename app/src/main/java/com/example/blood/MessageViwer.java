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
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MessageViwer extends AppCompatActivity {

    public String cloudRef = "RcvrInfo";
    Spinner spinner ;
    ListView listView;
    ImageButton buttonSearch;
    DatabaseReference databaseReference;
    List<BloodMessage> datatList;
    MessageViewAdapter messageViewAdapter;
    TextView textViewHeader;

    String bloodgrp;
    AlertDialog.Builder alertdialog;

    static  final  int REQUEST_CALL = 1;
    String PHONE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_viwer);
        casting();
    }
    private void casting() {

        buttonSearch=(ImageButton) findViewById(R.id.imageButtonSearch);
        spinner=(Spinner)findViewById(R.id.spinner_bloodMessage);
        listView=(ListView)findViewById(R.id.listview_bloodMessage);

        textViewHeader=(TextView)findViewById(R.id.textView1000);

        String[] bloodgrp=getResources().getStringArray(R.array.BloodGrp);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.sample_blood_grp,R.id.textViewSampleLayoutBloodgrp,bloodgrp);
        spinner.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference(cloudRef);
        datatList = new ArrayList<>();
        buttonSearch_clicked();
        listView_clicked();

        autoSrch();


    }

    private void autoSrch() {
        bloodgrp =  getIntent().getStringExtra("blood");
        textViewHeader.setText("Messages for Blood "+bloodgrp);
        messageViewAdapter =  new MessageViewAdapter(MessageViwer.this,datatList,bloodgrp);
        onStart();
        Toast.makeText(MessageViwer.this,"search complete",Toast.LENGTH_SHORT).show();


    }

    private void listView_clicked() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final BloodMessage data = datatList.get(position);



                alertdialog = new AlertDialog.Builder(MessageViwer.this);
                alertdialog.setTitle("Donner info");
                alertdialog.setPositiveButton("call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        makePhoneCall(data.phone);

                    }
                });

                alertdialog.setNeutralButton("Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MessageViwer.this,MapsActivity.class);
                        intent.putExtra("long",data.longitude);
                        intent.putExtra("lat",data.latitude);
                        startActivity(intent);

                    }
                });
                alertdialog.create().show();


            }
        });
    }

    private void buttonSearch_clicked() {
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bloodgrp = spinner.getSelectedItem().toString();
                textViewHeader.setText("Messages for Blood "+bloodgrp);
                messageViewAdapter =  new MessageViewAdapter(MessageViwer.this,datatList,bloodgrp);
                onStart();
                Toast.makeText(MessageViwer.this,"search complete",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                datatList.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    BloodMessage data = dataSnapshot1.getValue(BloodMessage.class);
                    datatList.add(data);
                }
                listView.setAdapter(messageViewAdapter) ;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();
    }
    private void makePhoneCall( String phone) {

        if(phone.trim().length()>0)
        {
            if(ContextCompat.checkSelfPermission(MessageViwer.this, Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(MessageViwer.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);

            }
            else
            {
                String s = "tel:"+phone;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(s)));

            }

        }
        else
        {
            Toast.makeText(MessageViwer.this,"empty",Toast.LENGTH_SHORT).show();

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
                Toast.makeText(MessageViwer.this,"permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }


}