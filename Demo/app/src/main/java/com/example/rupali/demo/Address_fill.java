package com.example.rupali.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Address_fill extends AppCompatActivity {

    static final int REQUEST_LOCATION=1;
    Geocoder geocoder=null;
    double latitude,longitude;
    private LocationManager locationManager=null;
    EditText edcity,edstate,edcountry,edpostCode,edaddress;
    String address,city,country,postalCode,state,knownName;
    List<Address> addresses = null;
    Button load_address,nextadd;
    Bundle bundle=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_fill);

        bundle=getIntent().getExtras();
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getlocation();

        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        edaddress=(EditText) findViewById(R.id.editText_address);
        edcity=(EditText)findViewById(R.id.editText_city);
        edstate=(EditText)findViewById(R.id.editText_state);
        edcountry=(EditText)findViewById(R.id.editText_country);
        edpostCode=(EditText)findViewById(R.id.editText_postcode);

        load_address=(Button)findViewById(R.id.button_loadAdd);
        nextadd=(Button)findViewById(R.id.buttonnextadd);

    }

    private void getlocation() {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)!=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
        }else
        {
            Location location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null)
            {
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                Toast.makeText(this, "here the address", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "address not found ! enter manually", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    public  void loadAddress(View view)
    {
        address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
        state = addresses.get(0).getAdminArea();
        country = addresses.get(0).getCountryName();
        postalCode = addresses.get(0).getPostalCode();
        knownName = addresses.get(0).getFeatureName();

        edaddress.setText(address);
        edcity.setText(city);
        edcountry.setText(country);
        edstate.setText(state);
        edpostCode.setText(postalCode);
    }

    public void goToPhoneVerification(View view)
    {
        bundle.putString("address",edaddress.getText().toString());
        Intent intent=new Intent(Address_fill.this,PhoneVerification.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
