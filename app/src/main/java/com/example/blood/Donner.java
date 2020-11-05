package com.example.blood;

public class Donner {

    String name ,age , phone , weight ,bloodgroup ,
            lastdonet ,gender ,month , year ,latitude ,longitude ,
            currentAddress , locality ,password;
    Donner()
    {

    }
    Donner( String name ,String age , String phone ,String weight ,String bloodgroup ,String lastdonet ,String gender ,String month,String year,
             String latitude ,String longitude ,String currentAddress ,String locality ,String password)
    {
        this.name =name;
        this.age =age;
        this.phone=phone;
        this.bloodgroup=bloodgroup;
        this.lastdonet=lastdonet;
        this.weight=weight;
        this.gender=gender;

        this.month=month;
        this.year = year;

        this.latitude = latitude ;
        this.longitude = longitude ;
         this.currentAddress=currentAddress ;
         this.locality= locality;

         this.password= password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCurrentAddress(String currentAddress) {
        this.currentAddress = currentAddress;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getCurrentAddress() {
        return currentAddress;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLocality() {
        return locality;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public void setLastdonet(String lastdonet) {
        this.lastdonet = lastdonet;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAge() {
        return age;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public String getLastdonet() {
        return lastdonet;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getWeight() {
        return weight;
    }
}
