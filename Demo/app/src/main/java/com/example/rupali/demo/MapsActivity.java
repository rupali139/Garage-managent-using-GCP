package com.example.rupali.demo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.rupali.demo.HelperClass.Garage;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback ,GoogleMap.OnInfoWindowClickListener {
    static final int REQUEST_LOCATION=1;
    private GoogleMap mMap;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private LatLng location=null;
    private GeoPoint grg = null;
    private LocationManager locationManager=null;
    double lati,longi;
    public static final String markerlat="com.example.application.example.markerlat";
    public static final String marketlon="com.example.application.example.markerlon";
    Bundle bundle=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        bundle=getIntent().getExtras();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
         db = FirebaseFirestore.getInstance();
         collectionReference=db.collection("Garage");
        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getlocation();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                Toast.makeText(MapsActivity.this, "Read Successful", Toast.LENGTH_SHORT).show();
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    Garage g= documentSnapshot.toObject(Garage.class);
                    grg =g.getLocation();
                    location=new LatLng(grg.getLatitude(),grg.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(location).title(g.getName()));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MapsActivity.this, "fail to read", Toast.LENGTH_SHORT).show();

            }
        });

        LatLng current=new LatLng(lati,longi);
        mMap.addMarker(new MarkerOptions().position(current).title("YOU ARE HERE"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current,17));
        mMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        LatLng clicklatlng=marker.getPosition();
        Intent intent=new Intent(this,InfoActivity.class);
        bundle.putString("Garagename",marker.getTitle());
        intent.putExtras(bundle);
        startActivity(intent);

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
                lati=location.getLatitude();
                longi=location.getLongitude();
            }
        }
    }
}
