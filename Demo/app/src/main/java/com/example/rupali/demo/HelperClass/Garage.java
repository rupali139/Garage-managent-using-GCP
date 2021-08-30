package com.example.rupali.demo.HelperClass;

import android.location.Address;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;
import java.util.Map;

public class Garage {
    String name,id;
    GeoPoint location;
    String phoneno;
    List<String> Time;
    Garage()
    {

    }
    Garage(String nm,GeoPoint gp,List<String> Time)
    {
        this.name=nm;
        this.location=gp;
        this.Time=Time;
    }

    public String getName() {
        return name;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getId() {
        return id;
    }


    public String getPhoneno() {
        return phoneno;
    }

    public List<String> getTime() {
        return Time;
    }
}
