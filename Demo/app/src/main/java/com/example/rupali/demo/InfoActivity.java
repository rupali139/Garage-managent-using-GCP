package com.example.rupali.demo;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rupali.demo.HelperClass.Garage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class InfoActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference collectionReference=null;
    private GeoPoint gpGarage=null;
    private  String garageId=null,name=null;
    private Garage garage;
    private String schedule;
    private List<String> time;
    Bundle bundle=null;

    public static final String IdGarage="com.example.application.example.garageId";
    String phoneno=null,address=null;
    Button btnAppointment;
    TextView tvname,tvAddress,tvphone,tvtime;
    Spinner spinnerTime;
    Geocoder geocoder;
    List<Address> addresses = null;
    String tag="";
    String[] day={   "   Monday"
                    ,"  Tuesday"
                    ,"Wednesday"
                    ," Thursday"
                    ,"   Friday"
                    ," Saturday"
                    ,"   Sunday"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        bundle=getIntent().getExtras();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        collectionReference = db.collection("Garage");
        tvname=(TextView)findViewById(R.id.tvname);
        tvAddress=(TextView)findViewById(R.id.tvaddress);
        tvphone=(TextView)findViewById(R.id.tvphoneno);
        tvtime=(TextView)findViewById(R.id.tvtime);
        getSelectedGarage();

    }



    public void getSelectedGarage()
    {
        Intent intent=getIntent();
        name=bundle.getString("Garagename");



        collectionReference.whereEqualTo("name",name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            String data="";
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    garage= documentSnapshot.toObject(Garage.class);
                    garageId=garage.getId();
                    phoneno=garage.getPhoneno();
                    time= garage.getTime();
                    gpGarage=garage.getLocation();
                    int i=0;
                    for(String tag: time)
                    {
                        data+="\n"+day[i]+"  :     "+tag.trim();
                        i++;
                    }
                    address=generateAddress(gpGarage);
                    tvphone.setText(phoneno);
                    tvname.setText(garage.getName());
                    tvAddress.setText(address);
                    String time =tvtime.getText().toString();
                    time+=data;
                    tvtime.setText(time);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InfoActivity.this,"Fail to Read", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String generateAddress(GeoPoint geoPoint)  {
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(geoPoint.getLatitude(),geoPoint.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        return address;
    }

    public void callGarage(View view)
    {
        Intent intent=new Intent(Intent.ACTION_DIAL);
        String phonenoFormat=String.format("tel: %s",phoneno);
        intent.setData(Uri.parse(phonenoFormat));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {

            Toast.makeText(this, "Can't resolve app for ACTION_DIAL Intent", Toast.LENGTH_SHORT).show();
        }
    }
    public void goToAppointment(View view)
    {
        Intent intent=new Intent(this,Appointment.class);
        bundle.putString("IdGarage",garageId);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
