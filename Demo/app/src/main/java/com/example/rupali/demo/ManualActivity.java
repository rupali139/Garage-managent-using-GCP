package com.example.rupali.demo;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.rupali.demo.HelperClass.Garage;
import com.example.rupali.demo.HelperClass.ListItem;
import com.example.rupali.demo.HelperClass.MyAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ManualActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private FirebaseFirestore db;
    private CollectionReference collectionReference;
    private Garage garage=null;
    Geocoder geocoder;
    List<Address> addresses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        db = FirebaseFirestore.getInstance();
        collectionReference=db.collection("Garage");

        recyclerView=(RecyclerView)findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listItems=new ArrayList<>();
        readData();

    }


    public void  readData()
    {
        collectionReference.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            String data="";
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot:queryDocumentSnapshots)
                {
                    garage= documentSnapshot.toObject(Garage.class);
                    ListItem listItem=new ListItem(garage.getName(),generateAddress(garage.getLocation()));
                    listItems.add(listItem);

                }
                adapter=new MyAdapter(listItems,ManualActivity.this);
                recyclerView.setAdapter(adapter);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ManualActivity.this,"Fail to Read", Toast.LENGTH_SHORT).show();
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

}
