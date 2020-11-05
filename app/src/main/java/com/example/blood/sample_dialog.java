package com.example.blood;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class sample_dialog extends AppCompatDialogFragment {
    @NonNull

    EditText editTextPhone,editTextPass;
    private  ExampleDialogListener listener;
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder =  new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.login_layout,null);
        builder.setView(view);

        builder.setTitle("Donner login");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String phone = editTextPhone.getText().toString();
                String pass = editTextPass.getText().toString();

                if(phone.isEmpty())
                {
                   Toast.makeText(getContext(),"phone number is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pass.isEmpty())
                {
                    Toast.makeText(getContext(),"password is empty",Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.applyTests(phone,pass);

            }
        });

        editTextPass=(EditText)view.findViewById(R.id.editTextTextPassword);
        editTextPhone=(EditText)view.findViewById(R.id.editTextPhone);


        return  builder.create();

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try{ listener =(ExampleDialogListener) context;}
        catch (ClassCastException e)
        {
            throw  new ClassCastException(context.toString()+
                    "must implement ExampleDialogListener");


        }

    }

    public  interface  ExampleDialogListener{
        void  applyTests(String phone,String pass);

    }



}
