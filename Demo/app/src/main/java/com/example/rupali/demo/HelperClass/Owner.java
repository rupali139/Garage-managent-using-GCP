package com.example.rupali.demo.HelperClass;

public class Owner {
    String name,Garageid,username,password;
    Owner()
    {

    }

    public void setGarageid(String garageid) {
        Garageid = garageid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getGarageid() {
        return Garageid;
    }

    public String getUsername() {
        return username;
    }
}
