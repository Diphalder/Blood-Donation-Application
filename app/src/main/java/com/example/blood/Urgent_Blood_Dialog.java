package com.example.blood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Urgent_Blood_Dialog extends AppCompatDialogFragment {

    Spinner spinnerbloodGrp;
    String [] bloodgrp;

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_urgent_blood,null);
        builder.setView(view);

        builder.setTitle("Urgent call");
        builder.setMessage("Select blood group for urgent call");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("find", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //data store
                String blood = spinnerbloodGrp.getSelectedItem().toString();
                Intent intent = new Intent(getContext(),viewAll.class);
                intent.putExtra("blood", blood);
                startActivity(intent);
                //Toast.makeText(getContext(),blood,Toast.LENGTH_SHORT).show();
            }
        });


        //casting
        bloodgrp=getResources().getStringArray(R.array.BloodGrp);
        spinnerbloodGrp = (Spinner)view.findViewById(R.id.spinner_1234);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.sample_blood_grp,R.id.textViewSampleLayoutBloodgrp,bloodgrp);
        spinnerbloodGrp.setAdapter(adapter);

        return  builder.create();

    }


}
