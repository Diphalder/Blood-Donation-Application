package com.example.blood;

import android.app.Activity;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageViewAdapter  extends ArrayAdapter<BloodMessage> {
    private Activity contest;
    private List<BloodMessage> dataList;
    String bloodNeed;




    public MessageViewAdapter( Activity context , List<BloodMessage>studentList,String bloodNeed) {
        super(context, R.layout.view_message_layout,studentList );
        this.contest = context;
        this.dataList = studentList;
        this.bloodNeed=bloodNeed;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = contest.getLayoutInflater();
        View view =  layoutInflater.inflate(R.layout.view_message_layout , null,true);
        BloodMessage data = dataList.get(position);
        TextView t1 = view.findViewById(R.id.textView11_Date);
        TextView t2 = view.findViewById(R.id.textView12_phone);
        TextView t3 = view.findViewById(R.id.textView13_location);

        if(bloodNeed.equals(data.bloodgrp))
        {
            t1.setText("date : " + data.date+"/"+data.month+"/"+data.year);
            t2.setText("phone : "+data.getPhone());
            t3.setText("Location : "+data.currentAddress);
        }
        else
        {
            view.setVisibility(View.GONE);
            t1.setWidth(0);
            t1.setHeight(0);
            t2.setWidth(0);
            t2.setHeight(0);
            t3.setWidth(0);
            t3.setHeight(0);
            view.setPadding(0,0,0,0);
        }


        return view;
    }


}
