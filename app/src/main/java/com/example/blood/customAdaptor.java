package com.example.blood;

import android.Manifest;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class customAdaptor extends ArrayAdapter<Donner> {
private Activity contest;
private List<Donner> dataList;
private  String blood;
String  latitude ,longitude , currentAddress ,countryName , locality , curYear, curMonth;




    public customAdaptor(Activity context , List<Donner>data , String blood, String latitude ,String longitude ,String currentAddress ,String countryName ,String locality,String curMonth , String curYear) {
        super(context, R.layout.sample_layout,data );
        this.contest = context;
        this.dataList = data;
       this.blood=blood;
        this.latitude = latitude ;
        this.longitude = longitude ;
        this.currentAddress=currentAddress ;
        this.locality= locality;
        this.countryName = countryName;
        this.curMonth=curMonth;
        this.curYear=curYear;
    }

    @Override
    public View getView(int position,  View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = contest.getLayoutInflater();
        View view =  layoutInflater.inflate(R.layout.sample_layout, null,true);
        Donner data = dataList.get(position);
        TextView t1 = view.findViewById(R.id.textViewName);
        TextView t2 = view.findViewById(R.id.textViewPhone);
        TextView t3 = view.findViewById(R.id.textViewLocation);


       if(data.getBloodgroup().equals(blood))
        {
            if(data.lastdonet.equals("more then 4")) {
                t1.setText("Name : " + data.getName());
                t2.setText("phone : " + data.getPhone());
                t3.setText("location : " + data.getCurrentAddress());
            }
            else if((Integer.valueOf(curYear)-Integer.valueOf(data.year))*12+Integer.valueOf(curMonth)-Integer.valueOf(data.month)>=4)
            {
                t1.setText("Name : " + data.getName());
                t2.setText("phone : " + data.getPhone());
                t3.setText("location : " + data.getCurrentAddress());

            }
            else
            {
                t1.setWidth(0);
                t1.setHeight(0);
                t2.setWidth(0);
                t2.setHeight(0);
                t3.setWidth(0);
                t3.setHeight(0);
            }


            }
       else
       {
           t1.setWidth(0);
           t1.setHeight(0);
           t2.setWidth(0);
           t2.setHeight(0);
           t3.setWidth(0);
           t3.setHeight(0);
       }

        return  view;
    }





}
