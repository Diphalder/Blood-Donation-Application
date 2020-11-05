package com.example.blood;

public class BloodMessage {
    String phone,bloodgrp,latitude,longitude,currentAddress,date,month,year;
    BloodMessage()
    {

    }
    BloodMessage(String phone,String bloodgrp,String latitude,String longitude,String currentAddress,String date,String month,String year)
    {
        this.bloodgrp=bloodgrp;
        this.currentAddress=currentAddress;
        this.longitude=longitude;
        this.latitude=latitude;
        this.phone=phone;
        this.date=date;
        this.month=month;
        this.year=year;

    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public void setBloodgrp(String bloodgrp) {
        this.bloodgrp = bloodgrp;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public String getBloodgrp() {
        return bloodgrp;
    }

    public String getPhone() {
        return phone;
    }

}
