package com.example.rupali.demo.HelperClass;

import com.google.firebase.Timestamp;

import java.util.Map;

public class BookAppointment {
    Object time;
    String vehical;
    String service,garageId,name,phoneno;
    public BookAppointment()
    {

    }

    public BookAppointment(Object time,String vehical,String service,String garageId,String name,String phoneno)
    {
        this.time = time;
        this.vehical=vehical;
        this.service=service;
        this.garageId=garageId;
        this.name=name;
        this.phoneno=phoneno;
    }
    public Object getTime() {
        return time;
    }

    public String getVehical() {
        return vehical;
    }

    public String getService() {
        return service;
    }

    public String getGarageId() {
        return garageId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneno() {
        return phoneno;
    }
}
