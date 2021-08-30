package com.example.rupali.demo.HelperClass;

public class ListItem {
    private String head;
    private String des;
    double latitude,longitude;
    public ListItem(String head, String des) {
        this.head = head;
        this.des = des;
        this.latitude=18.457608;
        this.longitude=73.850799;
    }

    public String getHead() {
        return head;
    }

    public String getDes() {
        return des;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
